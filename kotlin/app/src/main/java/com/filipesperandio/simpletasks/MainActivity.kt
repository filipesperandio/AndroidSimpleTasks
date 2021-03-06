package com.filipesperandio.simpletasks

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.filipesperandio.simpletasks.core.Task
import com.filipesperandio.simpletasks.core.TaskPresenter
import com.jakewharton.rxbinding2.view.RxView
import components.PresenterFactory
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private var tasksBus: PublishSubject<Task> = PublishSubject.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tasks.presenterFactory(TaskPresenterFactory())

        tasksBus.subscribe { tasks.append(it) }

        RxView.clicks(inputOk).subscribe {
            val text = inputTask.text.toString()

            if(text.isNotEmpty()) {
                tasksBus.onNext(Task(title = text, done = text.contains("done")))
                inputTask.text.clear()
            }
        };

    }
}

class TaskPresenterFactory : PresenterFactory {
    override fun create(parent: ViewGroup, viewType: Int, inflater: LayoutInflater): RecyclerView.ViewHolder {
        return TaskPresenter(parent, viewType, inflater);
    }

}

