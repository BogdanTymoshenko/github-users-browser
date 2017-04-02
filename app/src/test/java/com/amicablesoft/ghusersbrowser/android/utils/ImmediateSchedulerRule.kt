package com.amicablesoft.ghusersbrowser.android.utils

import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import rx.Scheduler
import rx.android.plugins.RxAndroidPlugins
import rx.android.plugins.RxAndroidSchedulersHook
import rx.plugins.RxJavaHooks
import rx.schedulers.Schedulers

class ImmediateSchedulerRule : TestRule {
    val scheduler = Schedulers.immediate()!!

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                RxJavaHooks.setOnIOScheduler { scheduler }
                RxJavaHooks.setOnComputationScheduler { scheduler }
                RxJavaHooks.setOnNewThreadScheduler { scheduler }
                RxAndroidPlugins.getInstance().registerSchedulersHook(object :  RxAndroidSchedulersHook() {
                    override fun getMainThreadScheduler(): Scheduler {
                        return scheduler
                    }
                })
                try {
                    base.evaluate()
                } finally {
                    RxJavaHooks.reset()
                    RxAndroidPlugins.getInstance().reset()
                }
            }
        }
    }
}