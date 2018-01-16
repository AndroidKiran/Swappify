package swapp.items.com.swappify.controllers.addgame.ui

import android.app.Activity.RESULT_OK
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPropertyAnimatorListener
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.webkit.URLUtil
import android.widget.AdapterView
import swapp.items.com.swappify.BR
import swapp.items.com.swappify.R
import swapp.items.com.swappify.common.AppUtils
import swapp.items.com.swappify.components.SlideAnimation
import swapp.items.com.swappify.components.TextChangeListener
import swapp.items.com.swappify.components.imagepicker.OverlapPickerActivity
import swapp.items.com.swappify.components.imagepicker.OverlapPickerActivity.Companion.URI
import swapp.items.com.swappify.components.imagepicker.Sources
import swapp.items.com.swappify.controllers.addgame.viewmodel.AddGameViewModel
import swapp.items.com.swappify.controllers.base.BaseFragment
import swapp.items.com.swappify.databinding.FragmentAddGameBinding
import javax.inject.Inject


class AddGameFragment : BaseFragment<FragmentAddGameBinding, AddGameViewModel>(),
        AppBarLayout.OnOffsetChangedListener {

    private var isAnchorHidden: Boolean = false
    private var isViewHidden: Boolean = false
    private var maxScrollSize = 0

    @Inject
    lateinit var viewFactory: ViewModelProvider.Factory

    @Inject
    lateinit var addGameViewModel: AddGameViewModel

    private lateinit var fragmentAddGameBinding: FragmentAddGameBinding

    override fun getLayoutId(): Int = R.layout.fragment_add_game

    override fun getViewModel(): AddGameViewModel {
        addGameViewModel = ViewModelProviders.of(this@AddGameFragment, viewFactory)
                .get(AddGameViewModel::class.java)
        return addGameViewModel
    }

    override fun executePendingVariablesBinding() {
        fragmentAddGameBinding = getViewDataBinding()
        fragmentAddGameBinding.setVariable(BR.addItemViewModel, addGameViewModel)
        fragmentAddGameBinding.setVariable(BR.textChangeCallBack, textWatcher)
        fragmentAddGameBinding.setVariable(BR.clickCallBack, onClickListener)
        fragmentAddGameBinding.executePendingBindings()
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentAddGameBinding.appbar.addOnOffsetChangedListener(this@AddGameFragment)
        fragmentAddGameBinding.summaryEditText.setOnTouchListener(onTouchListener)
        fragmentAddGameBinding.platformSpinner.onItemSelectedListener = onSpinnerItemSelectionListener
        fragmentAddGameBinding.releaseDateEditText.setManager(childFragmentManager)
        fragmentAddGameBinding.releaseDateEditText.setDateFormat(format = AppUtils.format)
    }


    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        if (maxScrollSize == 0)
            maxScrollSize = fragmentAddGameBinding.appbar.totalScrollRange

        val currentScrollPercentage = Math.abs(verticalOffset) * 100 / maxScrollSize

        scaleViewOnScroll(fragmentAddGameBinding.anchorLayout, currentScrollPercentage)
        handleVisibilityOnScroll(fragmentAddGameBinding.sliderView, currentScrollPercentage)
    }

    private fun scaleViewOnScroll(view: View?, scrollPercent: Int) {

        if (scrollPercent >= PERCENTAGE_TO_SHOW_ANCHOR) {
            if (!isAnchorHidden) {
                isAnchorHidden = true
                scaleAnimation(view = view, scaleFactor = MIN_SCALE_VALUE)
            }
        }

        if (scrollPercent < PERCENTAGE_TO_SHOW_ANCHOR) {
            if (isAnchorHidden) {
                isAnchorHidden = false
                scaleAnimation(view = view, scaleFactor = MAX_SCALE_VALUE)
            }
        }
    }

    private fun handleVisibilityOnScroll(view: View?, scrollPercent: Int) {

        if (scrollPercent >= PERCENTAGE_TO_SHOW_ANCHOR) {
            if (!isViewHidden) {
                isViewHidden = true
                translateAnimation(view, resources.getDimension(R.dimen.dimen_16).toInt())
            }
        }

        if (scrollPercent < PERCENTAGE_TO_SHOW_ANCHOR) {
            if (isViewHidden) {
                isViewHidden = false
                translateAnimation(view, resources.getDimension(R.dimen.dimen_110).toInt())
            }
        }
    }

    private fun translateAnimation(view: View?, targetMargin: Int) {
        val animation = SlideAnimation(view, targetMargin)
        animation.duration = TRANSLATE_ANIMATION_DURATION
        view?.startAnimation(animation)
    }

    private fun scaleAnimation(scaleFactor: Float, view: View?) {
        ViewCompat.animate(view)
                .scaleY(scaleFactor)
                .alpha(scaleFactor)
                .setInterpolator(AccelerateInterpolator())
                .setDuration(SCALE_ANIMATION_DURATION)
                .setListener(object : ViewPropertyAnimatorListener {
                    override fun onAnimationEnd(view: View?) {
                        if (scaleFactor == MIN_SCALE_VALUE) {
                            view?.visibility = View.GONE
                        }
                    }

                    override fun onAnimationCancel(view: View?) {
                        // DO nothing
                    }

                    override fun onAnimationStart(view: View?) {
                        if (scaleFactor == MAX_SCALE_VALUE) {
                            view?.visibility = View.VISIBLE
                        }
                    }

                })
                .start()
    }

    private fun handleOnTouch(view: View?, event: MotionEvent?): Boolean {
        view?.parent?.parent?.requestDisallowInterceptTouchEvent(true)
        when (event?.action!! and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_UP -> view?.parent?.parent?.requestDisallowInterceptTouchEvent(false)
        }
        return false
    }

    private var textWatcher: TextChangeListener = object : TextChangeListener() {

        override fun afterTextChanged(newValue: String?) {

            if (fragmentAddGameBinding.nameEditText.isFocused) {
                addGameViewModel.gameModel.get().setGameName(newValue)
            }

            if (fragmentAddGameBinding.developerEditText.isFocused) {
                addGameViewModel.gameModel.get().setGameDeveloper(newValue)
            }

            if (fragmentAddGameBinding.genreEditText.isFocused) {
                addGameViewModel.gameModel.get().setGameGenre(newValue)
            }

            if (fragmentAddGameBinding.publisherEditText.isFocused) {
                addGameViewModel.gameModel.get().setGamePlublisher(newValue)
            }

            if (fragmentAddGameBinding.releaseDateEditText.isFocused) {
                addGameViewModel.gameModel.get().setGameReleaseDate(newValue)
            }

            if (fragmentAddGameBinding.summaryEditText.isFocused) {
                addGameViewModel.gameModel.get().setGameSummary(newValue)
            }
        }
    }


    private val onSpinnerItemSelectionListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(adapter: AdapterView<*>?) {

        }

        override fun onItemSelected(adapter: AdapterView<*>?, view: View?, position: Int, l: Long) {
            if (position == -1) {
                return
            }

            val platform = addGameViewModel.platFormsList?.get(position)
            addGameViewModel.gameModel.get().setGamePlatform(platform)

        }
    }

    private val onTouchListener = View.OnTouchListener { view, motionEvent -> handleOnTouch(view, motionEvent) }

    private val onClickListener = object : IAddGameNavigator {

        override fun onAddGameClick() {
            if(addGameViewModel.validateGame()) {
                val url = addGameViewModel.gameModel.get().url
                if(URLUtil.isHttpsUrl(url) || URLUtil.isHttpUrl(url)) {
                    addGameViewModel.addGame()
                } else {
                    addGameViewModel.addGameWithImage("7204730956")
                }
            } else {
               fragmentAddGameBinding.appbar.setExpanded(true, true)
            }
        }

        override fun onCameraClick() {
            startPickerActivity(OverlapPickerActivity.TAKE_PHOTO, Sources.CAMERA)
        }

        override fun onGalleryClick() {
            startPickerActivity(OverlapPickerActivity.SELECT_PHOTO, Sources.GALLERY)
        }
    }

    fun startPickerActivity(requestCode: Int, source: Sources) {
        startActivityForResult(OverlapPickerActivity.start(context, source, false), requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == RESULT_OK) {
            val uriString = data?.getStringExtra(URI)
            if (!uriString.isNullOrEmpty()) {
                val uri = Uri.parse(uriString)
                addGameViewModel.gameModel.get().setGameUri("$uri")
            }
        }
    }

    companion object {
        const val PERCENTAGE_TO_SHOW_ANCHOR = 30
        const val SCALE_ANIMATION_DURATION: Long = 400
        const val TRANSLATE_ANIMATION_DURATION: Long = 600
        const val MAX_SCALE_VALUE = 1f
        const val MIN_SCALE_VALUE = 0f
    }

}