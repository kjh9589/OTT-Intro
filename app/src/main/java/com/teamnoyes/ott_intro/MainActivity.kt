package com.teamnoyes.ott_intro

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.marginLeft
import androidx.core.view.marginTop
import com.google.android.material.appbar.AppBarLayout
import com.teamnoyes.ott_intro.databinding.ActivityMainBinding
import kotlin.math.abs

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private var isGatheringMotionAnimating: Boolean = false
    private var isCurationMotionAnimating: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initAppbar()
        initToolbarContainerMargin()
        initScrollViewListeners()
        initMotionLayoutListeners()
    }

    private fun initAppbar() {
        binding.appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            // 처음에는 toolbar가 투명이다가 intro 이미지가 지나가면 불투명 이것을 부드럽게 하기
            val absVerticalOffset = abs(verticalOffset)
            binding.toolbarBackgroundView.alpha =
                absVerticalOffset / appBarLayout.totalScrollRange.toFloat()

            initActionBar()
        })
    }

    private fun initActionBar() = with(binding) {
        toolbar.navigationIcon = null
        toolbar.setContentInsetsAbsolute(0, 0)

        // toolbar를 액션바로 하여 틈 없앰
        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {
            it.setHomeButtonEnabled(false)
            it.setDisplayHomeAsUpEnabled(false)
            it.setDisplayShowHomeEnabled(false)
        }
    }


    private fun initToolbarContainerMargin() = with(binding) {
        // statusBar 높이만큼 toolbarConatiner에 marginTop 주기
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        val statusBarHeight = if (resourceId > 0) resources.getDimensionPixelSize(resourceId)
        else 100

        toolbarContainer.layoutParams =
            (toolbarContainer.layoutParams as ViewGroup.MarginLayoutParams).apply {
                setMargins(0, statusBarHeight, 0, 0)
            }

        collapsingToolbarContainer.layoutParams =
            (collapsingToolbarContainer.layoutParams as ViewGroup.MarginLayoutParams).apply {
                setMargins(0, 0, 0, 0)
            }

    }

    private fun initScrollViewListeners() = with(binding) {
        scrollView.smoothScrollTo(0, 0)

        // view가 그려진 뒤 값들을 얻기 위해 viewTreeObserver 이용
        scrollView.viewTreeObserver.addOnScrollChangedListener {
            val scrolledValue = scrollView.scrollY

            if (scrolledValue > 150f.dpToPx(this@MainActivity).toInt()) {
                // 내려갈 때
                if (!isGatheringMotionAnimating) {
                    gatheringDigitalThingsBackgroundMotionLayout.transitionToEnd()
                    gatheringDigitalThingsMotionLayout.transitionToEnd()
                    buttonShownMotionLayout.transitionToEnd()
                }
            } else {
                // 올라갈 때
                if (!isGatheringMotionAnimating) {
                    gatheringDigitalThingsBackgroundMotionLayout.transitionToStart()
                    gatheringDigitalThingsMotionLayout.transitionToStart()
                    buttonShownMotionLayout.transitionToStart()
                }
            }

            if (scrolledValue > scrollView.height) {
                if (!isCurationMotionAnimating) {
                    curationAnimationMotionLayout.setTransition(R.id.curation_animation_start1, R.id.curation_animation_end1)
                    curationAnimationMotionLayout.transitionToEnd()
                    isCurationMotionAnimating = true
                }
            }
        }
    }

    private fun initMotionLayoutListeners() = with(binding) {
        gatheringDigitalThingsMotionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
                isGatheringMotionAnimating = true
            }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {}

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                isGatheringMotionAnimating = false
            }

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}
        })

        curationAnimationMotionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {}

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {}

            override fun onTransitionCompleted(p0: MotionLayout?, currentId: Int) {
                when(currentId) {
                    R.id.curation_animation_end1 -> {
                        curationAnimationMotionLayout.setTransition(R.id.curation_animation_start2, R.id.curation_animation_end2)
                        curationAnimationMotionLayout.transitionToEnd()
                    }
                }
            }

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}
        })
    }
}