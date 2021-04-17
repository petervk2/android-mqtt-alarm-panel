/*
 * Copyright (c) 2018 ThanksMister LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.thanksmister.iot.mqtt.alarmpanel.di;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.view.LayoutInflater;

import androidx.preference.PreferenceManager;

import com.thanksmister.iot.mqtt.alarmpanel.network.ImageOptions;
import com.thanksmister.iot.mqtt.alarmpanel.network.MQTTOptions;
import com.thanksmister.iot.mqtt.alarmpanel.persistence.Configuration;
import com.thanksmister.iot.mqtt.alarmpanel.persistence.SensorDao;
import com.thanksmister.iot.mqtt.alarmpanel.utils.DialogUtils;
import com.thanksmister.iot.mqtt.alarmpanel.utils.NotificationUtils;
import com.thanksmister.iot.mqtt.alarmpanel.utils.ScreenUtils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
class ActivityModule {

    @Provides
    static DialogUtils providesDialogUtils(Application application) {
        return new DialogUtils(application);
    }

    @Provides
    static Resources providesResources(Application application) {
        return application.getResources();
    }

    @Provides
    static LayoutInflater providesInflater(Application application) {
        return (LayoutInflater) application.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(Application app) {
        return PreferenceManager.getDefaultSharedPreferences(app.getApplicationContext());
    }

    @Provides
    static Configuration provideConfiguration(Application application, SharedPreferences preference) {
        return new Configuration(application, preference);
    }

    @Provides
    static MQTTOptions provideMQTTOptions(SharedPreferences preference, SensorDao sensorDao) {
        return new MQTTOptions(preference, sensorDao);
    }

    @Provides
    static NotificationUtils notificationUtils(Application application) {
        return new NotificationUtils(application);
    }

    @Provides
    static ScreenUtils screenUtils(Application application, Configuration configuration) {
        return new ScreenUtils(application, configuration);
    }

    @Provides
    static ImageOptions provideImageOptions(SharedPreferences preference) {
        return new ImageOptions(preference);
    }
}