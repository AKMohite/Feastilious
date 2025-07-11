// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.core.notification.di

import com.ak.feastit.core.notification.NotificationManager
import com.ak.feastit.core.notification.YumNotificationManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class NotificationModule {
  @Binds
  abstract fun bindNotificationManager(manager: YumNotificationManager): NotificationManager
}
