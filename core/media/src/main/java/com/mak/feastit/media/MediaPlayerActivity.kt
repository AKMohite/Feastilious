package com.mak.feastit.media

import android.app.Dialog
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.dash.DashMediaSource
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.rtsp.RtspMediaSource
import androidx.media3.exoplayer.smoothstreaming.SsMediaSource
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.exoplayer.trackselection.MappingTrackSelector
import androidx.media3.ui.AspectRatioFrameLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mak.feastit.media.databinding.ActivityMediaPlayerBinding
import com.mak.feastit.media.databinding.CustomControlsBinding
import java.util.Locale
import java.util.Objects

@UnstableApi
class MediaPlayerActivity : AppCompatActivity() {

  private var _binding: ActivityMediaPlayerBinding? = null
  private val binding
    get() = _binding!!

  private var _customBinding: CustomControlsBinding? = null
  private val customBinding
    get() = _customBinding!!

  private lateinit var exoPlayer: ExoPlayer
  private lateinit var dataSourceFactory: DataSource.Factory
  private var isFullScreen = false
  private var RESIZE_MODE = 0
  private var setResizeTxt: String = "Original"
  private var setQualityTxt: String = "Auto"
  private var setSpeedText: String = "1x"
  private var setAudioText: String = "Auto"
  private var setSubtitleText: String = "Off"
  private lateinit var defaultTrackSelector: DefaultTrackSelector
  private var videoQualities = emptyList<String>()
  private var audioManager: AudioManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = false
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightNavigationBars = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
          window.isNavigationBarContrastEnforced = false
        }
        _binding = ActivityMediaPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

      val url = intent.getStringExtra("url")
      val uri = url?.toUri()
      if (uri == null) {
        finish()
        return
      }
      dataSourceFactory = DefaultHttpDataSource.Factory()
      defaultTrackSelector = DefaultTrackSelector(this)
      defaultTrackSelector.setParameters(
        defaultTrackSelector.buildUponParameters()
          .setTrackTypeDisabled(C.TRACK_TYPE_TEXT, true)
          .build()
      )
      val mediaSource = buildMediaSource(uri)

      exoPlayer = ExoPlayer.Builder(this)
        .setTrackSelector(defaultTrackSelector)
        .build()
      binding.playerView.player = exoPlayer
      exoPlayer.setMediaSource(mediaSource)
      exoPlayer.prepare()
      exoPlayer.playWhenReady = true
      exoPlayer.play()

      binding.playerView.showController()

      val controller = binding.playerView.findViewById<RelativeLayout>(R.id.custom_controls_root)
      _customBinding = CustomControlsBinding.bind(controller)

//        val playPauseBtn = binding.playerView.findViewById<ImageView>(R.id.exo_play_pause_btn)
//        val backBtn = binding.playerView.findViewById<ImageView>(R.id.back_btn)
//        val fullBtn = binding.playerView.findViewById<ImageView>(R.id.full_btn)
//        val tenLeftBtn = binding.playerView.findViewById<ImageView>(R.id.ten_left_btn)
//        val tenRightBtn = binding.playerView.findViewById<ImageView>(R.id.ten_right_btn)
//        val exoTitle = binding.playerView.findViewById<TextView>(R.id.exo_title)
//        val lockBtn = binding.playerView.findViewById<ImageView>(R.id.lock_btn)
//        val unlockBtn = binding.playerView.findViewById<ImageView>(R.id.unlock_btn)
//        val resizeBtn = binding.playerView.findViewById<ImageView>(R.id.resize_btn)
//        val customControls = binding.playerView.findViewById<ConstraintLayout>(R.id.custom_controls)
//        val linearLayout = binding.playerView.findViewById<LinearLayout>(R.id.linear_settings)
//        val settingsBtn = binding.playerView.findViewById<ImageView>(R.id.setting_btn)
//        val speedBtn = binding.playerView.findViewById<LinearLayout>(R.id.speed_btn)
//        val speedTxt = binding.playerView.findViewById<TextView>(R.id.speed_txt)

      customBinding.exoTitle.text = "Video name"

      customBinding.settingBtn.setOnClickListener {
        showSettingsDialog()
      }

      customBinding.exoPlayPauseBtn.setOnClickListener {
        if (exoPlayer.isPlaying) {
          exoPlayer.pause()
          customBinding.exoPlayPauseBtn.setImageResource(R.drawable.ic_play)
        } else {
          exoPlayer.play()
          customBinding.exoPlayPauseBtn.setImageResource(R.drawable.ic_pause)
        }
      }

      customBinding.resizeBtn.setOnClickListener {
        resizeScreen()
      }

      customBinding.speedBtn.setOnClickListener {
        showSpeedDialog()
      }

      customBinding.qualityBtn.setOnClickListener {
        showQualityDialog()
      }

      customBinding.lockBtn.setOnClickListener {
        customBinding.customControls.visibility = View.GONE
        customBinding.unlockBtn.visibility = View.VISIBLE
      }

      customBinding.unlockBtn.setOnClickListener {
        customBinding.customControls.visibility = View.VISIBLE
        customBinding.unlockBtn.visibility = View.GONE
      }

      customBinding.audioBtn.setOnClickListener {
        showAudioDialog()
      }

      customBinding.subtitleBtn.setOnClickListener {
        showSubtitleDialog()
      }

      audioManager = getSystemService(AUDIO_SERVICE) as? AudioManager
      val maxVolume = audioManager?.getStreamMaxVolume(AudioManager.STREAM_MUSIC) ?: 0
      val currentVolume = audioManager?.getStreamVolume(AudioManager.STREAM_MUSIC) ?: 0

      customBinding.volumeSeekbar.max = maxVolume
      customBinding.volumeSeekbar.progress = currentVolume
      customBinding.volumeSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(
          seekbar: SeekBar?,
          progress: Int,
          fromUser: Boolean
        ) {
          audioManager?.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0)
        }

        override fun onStartTrackingTouch(p0: SeekBar?) {

        }

        override fun onStopTrackingTouch(p0: SeekBar?) {

        }

      })

      customBinding.brightnessSeekbar.max = 255
      customBinding.brightnessSeekbar.progress = 10
      customBinding.brightnessSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(
          seekbar: SeekBar?,
          progress: Int,
          fromUser: Boolean
        ) {
          val brightness = progress / 255f
          val layoutParams = window.attributes
          layoutParams.screenBrightness = brightness
          window.attributes = layoutParams
        }

        override fun onStartTrackingTouch(p0: SeekBar?) {

        }

        override fun onStopTrackingTouch(p0: SeekBar?) {

        }

      })

      customBinding.fullBtn.setOnClickListener {
        if (isFullScreen) {
          customBinding.fullBtn.setImageDrawable(ContextCompat.getDrawable(this@MediaPlayerActivity, R.drawable.full_close))
          customBinding.exoTitle.visibility = View.INVISIBLE
          customBinding.lockBtn.visibility = View.GONE
          customBinding.settingBtn.visibility = View.VISIBLE
          customBinding.linearSettings.visibility = View.GONE
          customBinding.volumeLl.visibility = View.GONE
          customBinding.brightnessLl.visibility = View.GONE
          supportActionBar?.show()
          customBinding.resizeBtn.visibility = View.GONE
          window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
          requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
          val params = binding.playerView.layoutParams as? ConstraintLayout.LayoutParams
          params?.width = ViewGroup.LayoutParams.MATCH_PARENT
          params?.height = (200 * applicationContext.resources.displayMetrics.density).toInt()
          binding.playerView.layoutParams = params
        } else {
          customBinding.fullBtn.setImageDrawable(ContextCompat.getDrawable(this@MediaPlayerActivity, R.drawable.full_open))
          customBinding.exoTitle.visibility = View.VISIBLE
          customBinding.lockBtn.visibility = View.VISIBLE
          customBinding.resizeBtn.visibility = View.VISIBLE
          customBinding.settingBtn.visibility = View.GONE
          customBinding.linearSettings.visibility = View.VISIBLE
          customBinding.volumeLl.visibility = View.VISIBLE
          customBinding.brightnessLl.visibility = View.VISIBLE
          supportActionBar?.hide()
          window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
          requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
          val params = binding.playerView.layoutParams as? ConstraintLayout.LayoutParams
          params?.width = ViewGroup.LayoutParams.MATCH_PARENT
          params?.height = ViewGroup.LayoutParams.MATCH_PARENT
          binding.playerView.layoutParams = params
        }
        isFullScreen = !isFullScreen
      }

      // WARN: we are performing click programmatically to launch in full screen directly
      customBinding.fullBtn.performClick()

      customBinding.backBtn.setOnClickListener {
        finish()
      }
      customBinding.tenLeftBtn.setOnClickListener {
        val num = exoPlayer.currentPosition - 10_000
        if (num < 0) {
          exoPlayer.seekTo(0L)
        } else {
          exoPlayer.seekTo(num)
        }
      }

      customBinding.tenRightBtn.setOnClickListener {
        val num = exoPlayer.currentPosition + 10_000
        if (num < 0) {
          exoPlayer.seekTo(0L)
        } else {
          exoPlayer.seekTo(num)
        }
      }

      exoPlayer.addListener(object  : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
          when (playbackState) {
            Player.STATE_READY -> {
              binding.progressBar.visibility = View.GONE
              customBinding.exoPlayPauseBtn.visibility = View.VISIBLE
              videoQualities = getVideoQualityTracks()
            }
            Player.STATE_BUFFERING -> {
              binding.progressBar.visibility = View.VISIBLE
              customBinding.exoPlayPauseBtn.visibility = View.VISIBLE
            }
            else -> {
              binding.progressBar.visibility = View.GONE
              binding.playerView.showController()
            }
          }
        }
      })
    }

  private var selectedSubtitleIndex = 0
  fun showSubtitleDialog() {
    val tracks = exoPlayer.currentTracks
    if (tracks.isEmpty) {
      Toast.makeText(this, "No subtitles available", Toast.LENGTH_SHORT).show()
      return
    }
    val subtitleTrackGroups = mutableListOf<String>()
    val subtitleList = mutableListOf<String>()
    subtitleList.add("off")
    for (i in 0 until tracks.groups.size) {
      val trackGroup = tracks.groups[i]
      val trackType = trackGroup.type
      if (trackType == C.TRACK_TYPE_TEXT) {
        for (i in 0 until trackGroup.length) {
          val isSupported = trackGroup.isTrackSupported(i)
          val trackFormat = trackGroup.getTrackFormat(i)
          val language = trackFormat.language
          val supported = if (isSupported) {
            ""
          } else {
            " (Unsupported)"
          }
          val displayName: String
          val languageCode: String

          if (!language.isNullOrBlank() && !language.equals("und", true)) {
            if (language.contains("-")) {
              languageCode = language.split("-").first()
            } else {
              languageCode = language
            }
            displayName = Locale(languageCode).displayLanguage + supported
          } else {
            languageCode = "track_${i + 1}"
            displayName = "Unknown subtitle ${i + 1} $supported"
          }
          subtitleTrackGroups.add(languageCode)
          subtitleList.add(displayName)
        }
      }
    }
    val tempTracks = subtitleList
    MaterialAlertDialogBuilder(this, R.style.DialogTheme)
      .setTitle("Subtitle")
      .setSingleChoiceItems(tempTracks.toTypedArray(), selectedSubtitleIndex, {dialog, which ->
        selectedSubtitleIndex = which
      }).setPositiveButton("Ok", {dialog, which ->
        if (selectedSubtitleIndex == 0){
          defaultTrackSelector.setParameters(
            defaultTrackSelector.buildUponParameters()
              .setTrackTypeDisabled(C.TRACK_TYPE_TEXT, true)
              .build()
          )
          setSubtitleText = "Off"
          customBinding.subtitleTxt.text = setSubtitleText
        } else {
          defaultTrackSelector.setParameters(
            defaultTrackSelector.buildUponParameters()
              .setTrackTypeDisabled(C.TRACK_TYPE_TEXT, false)
              .setPreferredTextLanguage(subtitleTrackGroups[selectedAudioIndex])
              .build()
          )
          setSubtitleText = tempTracks[selectedSubtitleIndex]
          customBinding.subtitleTxt.text = setSubtitleText
        }
      })
      .setNegativeButton("Cancel", null)
      .show()
  }

  private var selectedAudioIndex = 0
  private fun showAudioDialog() {
    val tracks = exoPlayer.currentTracks
    if (tracks.isEmpty) {
      Toast.makeText(this, "No audio tracks available", Toast.LENGTH_SHORT).show()
      return
    }
    val audioTrackGroups = mutableListOf<String>()
    val audioList = mutableListOf<String>()
    for (i in 0 until tracks.groups.size) {
      val trackGroup = tracks.groups[i]
      val trackType = trackGroup.type
      if (trackType == C.TRACK_TYPE_AUDIO) {
        for (i in 0 until trackGroup.length) {
          val isSupported = trackGroup.isTrackSupported(i)
          val trackFormat = trackGroup.getTrackFormat(i)
          val language = trackFormat.language
          val supported = if (isSupported) {
            ""
          } else {
            " (Unsupported)"
          }
          val displayName: String
          val languageCode: String

          if (!language.isNullOrBlank() && !language.equals("und", true)) {
            if (language.contains("-")) {
              languageCode = language.split("-").first()
            } else {
              languageCode = language
            }
            displayName = Locale(languageCode).displayLanguage + supported
          } else {
            languageCode = "track_${i + 1}"
            displayName = "Unknown audio ${i + 1} $supported"
          }
          audioTrackGroups.add(languageCode)
          audioList.add(displayName)
        }
      }
    }
    val tempTracks = audioList
    MaterialAlertDialogBuilder(this, R.style.DialogTheme)
      .setTitle("Audio tracks")
      .setSingleChoiceItems(tempTracks.toTypedArray(), selectedAudioIndex, {dialog, which ->
        selectedAudioIndex = which
      }).setPositiveButton("Ok", {dialog, which ->
        defaultTrackSelector.setParameters(
          defaultTrackSelector.buildUponParameters()
            .setPreferredAudioLanguage(audioTrackGroups[selectedAudioIndex])
            .build()
        )
        setAudioText = tempTracks[selectedAudioIndex]
        customBinding.audioTxt.text = setAudioText
      })
      .setNegativeButton("Cancel", null)
      .show()
  }

  fun isSupportedFormat(renderTrack: MappingTrackSelector.MappedTrackInfo, renderIndex: Int): Boolean {
    val trackGroupArray = renderTrack.getTrackGroups(renderIndex)
    return if (trackGroupArray.length == 0) {
      false
    } else {
      renderTrack.getRendererType(renderIndex) == C.TRACK_TYPE_VIDEO
    }
  }

  private fun getVideoQualityTracks(): List<String> {
    val videoQualities = mutableListOf<String>()
    val renderTrack = defaultTrackSelector.currentMappedTrackInfo
    renderTrack?.let { trackInfo ->

      val renderCount = trackInfo.rendererCount
      for (renderIndex in 0 until renderCount) {
        if (isSupportedFormat(renderTrack, renderIndex)) {
          val trackGroupType = renderTrack.getRendererType(renderIndex)
          val trackGroups = renderTrack.getTrackGroups(renderIndex)
          val trackGroupsCount = trackGroups.length
          if (trackGroupType == C.TRACK_TYPE_VIDEO) {
            for (groupIndex in 0 until trackGroupsCount) {
              val videoQualityTrackCount = trackGroups.get(groupIndex).length
              for (trackIndex in 0 until videoQualityTrackCount) {
                val isTrackSupported = renderTrack.getTrackSupport(renderIndex, groupIndex, trackIndex) == C.FORMAT_HANDLED
                if (isTrackSupported) {
                  val trackGroup = trackGroups.get(groupIndex)
                  val videoWidth = trackGroup.getFormat(trackIndex).width
                  val videoHeight = trackGroup.getFormat(trackIndex).height
                  val quality = "$videoWidth x $videoHeight"
                  videoQualities.add(quality)
                }
              }
            }
          }
        }
      }
    }
    if (videoQualities.isNotEmpty()) videoQualities.add(0, "Auto")
    return videoQualities
  }

  @UnstableApi
  fun showSettingsDialog() {
    val dialog = Dialog(this)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(R.layout.bottom_sheet)
    val ratioText = dialog.findViewById<TextView>(R.id.ratio_text)
    val speedText = dialog.findViewById<TextView>(R.id.speed_text)
    val qualityText = dialog.findViewById<TextView>(R.id.quality_text)
    val audioText = dialog.findViewById<TextView>(R.id.audio_text)
    val subtitleText = dialog.findViewById<TextView>(R.id.subtitle_text)
    qualityText.text = setQualityTxt
    ratioText.text = setResizeTxt
    speedText.text = setSpeedText
    audioText.text = setAudioText
    subtitleText.text = setSubtitleText

    dialog.findViewById<LinearLayout>(R.id.quality_dialog).setOnClickListener {
      showQualityDialog()
      dialog.dismiss()
    }

    dialog.findViewById<LinearLayout>(R.id.ratio_dialog).setOnClickListener {
      resizeScreen()
      dialog.dismiss()
    }

    dialog.findViewById<LinearLayout>(R.id.speed_dialog).setOnClickListener {
      showSpeedDialog()
      dialog.dismiss()
    }
    dialog.findViewById<LinearLayout>(R.id.audio_dialog).setOnClickListener {
      showAudioDialog()
      dialog.dismiss()
    }
    dialog.findViewById<LinearLayout>(R.id.subtitle_dialog).setOnClickListener {
      showSubtitleDialog()
      dialog.dismiss()
    }
    Objects.requireNonNull(dialog.window)?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
      ViewGroup.LayoutParams.MATCH_PARENT)
    dialog.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
    dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
    dialog.window?.setGravity(Gravity.BOTTOM)
    dialog.show()
  }

  private val speeds = arrayOf("0.5x", "0.75x", "1x", "1.5x", "1.75x", "2x")
  private val speedValues = arrayOf(0.5f, 0.75f, 1f, 1.5f, 1.75f, 2f)
  private var selectedIndex = 2
  private fun showSpeedDialog() {
    MaterialAlertDialogBuilder(this, R.style.DialogTheme)
      .setTitle("Playback speed")
      .setSingleChoiceItems(speeds, selectedIndex, { dialog, which ->
        selectedIndex = which
      })
      .setPositiveButton("Ok", {dialog, which ->
        val speed = speedValues[selectedIndex]
        if (speed < 0f) {
          exoPlayer.playbackParameters = PlaybackParameters(speed)
        } else {
          exoPlayer.playbackParameters = exoPlayer.playbackParameters.withSpeed(speed)
        }
        setSpeedText = speeds[selectedIndex]
        customBinding.speedTxt.text = setSpeedText
      }).setNegativeButton("Cancel", null)
      .show()
  }

  private var selectedQualityIndex = 0
  private fun showQualityDialog() {
    if (videoQualities.isNotEmpty()) {
      MaterialAlertDialogBuilder(this, R.style.DialogTheme)
        .setTitle("Video quality")
        .setSingleChoiceItems(videoQualities.toTypedArray(), selectedQualityIndex, {dialog, which ->
          selectedQualityIndex = which
          setQualityTxt = "Auto"
          customBinding.qualityTxt.text = setQualityTxt
        })
        .setPositiveButton("Ok", {dialog, which ->
          if (selectedQualityIndex == 0) {
            defaultTrackSelector.setParameters(defaultTrackSelector.buildUponParameters().setMaxVideoSizeSd())
          } else {
            val qualityInfo = videoQualities[selectedQualityIndex].split(" x ")
            val videoWidth = qualityInfo.first().toInt()
            val videoHeight = qualityInfo.last().toInt()
            defaultTrackSelector.setParameters(
              defaultTrackSelector.buildUponParameters()
                .setMaxVideoSize(videoWidth, videoHeight)
                .setMinVideoSize(videoWidth, videoHeight)
            )
            setQualityTxt = "${qualityInfo[1]} p"
            customBinding.qualityTxt.text = setQualityTxt
          }
        })
        .setNegativeButton("Cancel", null)
        .show()
    } else {
      Toast.makeText(this, "No video qualities available", Toast.LENGTH_SHORT).show()
    }
  }

  @UnstableApi
  private fun resizeScreen() {
    when (RESIZE_MODE) {
      0 -> {
        binding.playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
        RESIZE_MODE = 1
        showResizeNotice("Zoomed to fill")
      }
      1 -> {
        binding.playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT
        RESIZE_MODE = 2
        showResizeNotice("Fixed Height")
      }
      2 -> {
        binding.playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH
        RESIZE_MODE = 3
        showResizeNotice("Fixed Width")
      }
      3 -> {
        binding.playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
        RESIZE_MODE = 4
        showResizeNotice("Zoom")
      }
      4 -> {
        binding.playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
        RESIZE_MODE = 0
        showResizeNotice("Original")
      }
    }

  }

  fun showResizeNotice(text: String) {
    with(binding.resizeTxt) {
      setText(text)
      setResizeTxt = text
      visibility = View.VISIBLE
      alpha = 1f
      animate()
        .alpha(0f)
        .setDuration(1_000)
        .setStartDelay(700)
        .withEndAction {
          visibility = View.GONE
        }.start()
    }
  }

  @UnstableApi
  private fun buildMediaSource(uri: Uri): MediaSource {
    val contentType = Util.inferContentType(uri)
    val mediaItem = MediaItem.fromUri(uri)
    return when (contentType) {
        C.CONTENT_TYPE_HLS -> {
          HlsMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)
        }
        C.CONTENT_TYPE_DASH -> {
          DashMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)
        }
        C.CONTENT_TYPE_SS -> {
          SsMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)
        }
        C.CONTENT_TYPE_RTSP -> {
          RtspMediaSource.Factory().createMediaSource(mediaItem)
        }
        else -> {
          ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)
        }
    }
  }

  override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
    if (keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
      val currentVolume = audioManager?.getStreamVolume(AudioManager.STREAM_MUSIC) ?: 0
      customBinding.volumeSeekbar.progress = currentVolume
    }
    return super.onKeyDown(keyCode, event)
  }

  override fun onResume() {
    exoPlayer.play()
    super.onResume()
  }

  override fun onPause() {
    exoPlayer.pause()
    super.onPause()
  }

  override fun onDestroy() {
    exoPlayer.release()
    _customBinding = null
    _binding = null
    super.onDestroy()
  }
}
