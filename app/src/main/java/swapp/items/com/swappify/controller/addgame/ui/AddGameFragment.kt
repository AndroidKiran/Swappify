package swapp.items.com.swappify.controller.addgame.ui

import android.app.Activity.RESULT_OK
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.text.Editable
import android.view.MotionEvent
import android.view.View
import android.webkit.URLUtil
import android.widget.AdapterView
import swapp.items.com.swappify.BR
import swapp.items.com.swappify.R
import swapp.items.com.swappify.common.AppUtils
import swapp.items.com.swappify.common.extension.scaleAnimation
import swapp.items.com.swappify.common.extension.translateAnimation
import swapp.items.com.swappify.components.imagepicker.OverlapPickerActivity
import swapp.items.com.swappify.components.imagepicker.OverlapPickerActivity.Companion.URI
import swapp.items.com.swappify.components.imagepicker.Sources
import swapp.items.com.swappify.controller.addgame.viewmodel.AddGameViewModel
import swapp.items.com.swappify.controller.base.BaseFragment
import swapp.items.com.swappify.databinding.FragmentAddGameBinding
import javax.inject.Inject


class AddGameFragment : BaseFragment<FragmentAddGameBinding, AddGameViewModel>(), AppBarLayout.OnOffsetChangedListener {

    private var isAnchorHidden = false
    private var isViewHidden = false
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
        fragmentAddGameBinding.setVariable(BR.viewCallBack, listener)
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
                view?.scaleAnimation(scaleFactor = MIN_SCALE_VALUE)
            }
        }

        if (scrollPercent < PERCENTAGE_TO_SHOW_ANCHOR) {
            if (isAnchorHidden) {
                isAnchorHidden = false
                view?.scaleAnimation(scaleFactor = MAX_SCALE_VALUE)
            }
        }
    }

    private fun handleVisibilityOnScroll(view: View?, scrollPercent: Int) {

        if (scrollPercent >= PERCENTAGE_TO_SHOW_ANCHOR) {
            if (!isViewHidden) {
                isViewHidden = true
                view?.translateAnimation(resources.getDimension(R.dimen.dimen_16).toInt())
            }
        }

        if (scrollPercent < PERCENTAGE_TO_SHOW_ANCHOR) {
            if (isViewHidden) {
                isViewHidden = false
                view?.translateAnimation(resources.getDimension(R.dimen.dimen_110).toInt())
            }
        }
    }

    private fun handleOnTouch(view: View?, event: MotionEvent?): Boolean {
        view?.parent?.parent?.requestDisallowInterceptTouchEvent(true)
        when (event?.action!! and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_UP -> view?.parent?.parent?.requestDisallowInterceptTouchEvent(false)
        }
        return false
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

    private val listener = object : IAddGameNavigator {

        override fun afterTextChanged(editable: Editable) {

            if (fragmentAddGameBinding.nameEditText.isFocused) {
                addGameViewModel.gameModel.get().setGameName(editable.toString())
            }

            if (fragmentAddGameBinding.developerEditText.isFocused) {
                addGameViewModel.gameModel.get().setGameDeveloper(editable.toString())
            }

            if (fragmentAddGameBinding.genreEditText.isFocused) {
                addGameViewModel.gameModel.get().setGameGenre(editable.toString())
            }

            if (fragmentAddGameBinding.publisherEditText.isFocused) {
                addGameViewModel.gameModel.get().setGamePlublisher(editable.toString())
            }

            if (fragmentAddGameBinding.releaseDateEditText.isFocused) {
                addGameViewModel.gameModel.get().setGameReleaseDate(editable.toString())
            }

            if (fragmentAddGameBinding.summaryEditText.isFocused) {
                addGameViewModel.gameModel.get().setGameSummary(editable.toString())
            }
        }

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