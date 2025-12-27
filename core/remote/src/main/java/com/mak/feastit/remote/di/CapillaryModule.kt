package com.mak.feastit.remote.di

class CapillaryModule {
  companion object {
    init { System.loadLibrary("native-lib") }
  }

  external fun getApiKey(): String
}
