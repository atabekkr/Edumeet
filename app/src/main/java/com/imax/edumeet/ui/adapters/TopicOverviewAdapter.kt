package com.imax.edumeet.ui.adapters

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.imax.edumeet.R
import com.imax.edumeet.databinding.ItemEmptyBinding
import com.imax.edumeet.databinding.ItemHeadlineBinding
import com.imax.edumeet.databinding.ItemImageBinding
import com.imax.edumeet.databinding.ItemMaterialAudioBinding
import com.imax.edumeet.databinding.ItemTextBinding
import com.imax.edumeet.models.Content
import com.imax.edumeet.utils.getResourceId
import com.imax.edumeet.utils.milliSecondsToTimer


class TopicOverviewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Handler.Callback {

    var lifecycle: Lifecycle? = null

    var player: MediaPlayer? = null

    private var playingHolder: AudioViewHolder? = null

    private var playingAudioPos = -1

    private val MSG_UPDATE_SEEK_BAR = 1845
    var uiUpdateHandler: Handler? = null

    companion object {
        const val IMAGE = 0
        const val TEXT = 1
        const val HEADLINE = 2
        const val AUDIO = 3
        const val VIDEO = 4
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TEXT -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_text, parent, false)
                val binding = ItemTextBinding.bind(view)
                TextViewHolder(binding)
            }

            HEADLINE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_headline, parent, false)
                val binding = ItemHeadlineBinding.bind(view)
                TextHeadlineViewHolder(binding)
            }

            IMAGE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_image, parent, false)
                val binding = ItemImageBinding.bind(view)
                ImageViewHolder(binding)
            }

            AUDIO -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_material_audio, parent, false)
                val binding = ItemMaterialAudioBinding.bind(view)
                AudioViewHolder(binding)
            }

            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_empty, parent, false)
                val binding = ItemEmptyBinding.bind(view)
                EmptyViewHolder(binding)
            }
        }
    }


    inner class AudioViewHolder(private val binding: ItemMaterialAudioBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private fun updateSeekBar() {
            player?.let {
                val currentPosition = it.currentPosition
                binding.sbController.progress = currentPosition
                binding.tvCurrentTime.text = currentPosition.toLong().milliSecondsToTimer()
                binding.tvFullTime.text =
                    "/ ${it.duration.toLong().milliSecondsToTimer()}"

                // Планируем следующее обновление через 1000 мс
                uiUpdateHandler?.sendEmptyMessageDelayed(MSG_UPDATE_SEEK_BAR, 1000)
            }
        }

        fun bind() {
            uiUpdateHandler = Handler(Looper.getMainLooper()) { msg ->
                if (msg.what == MSG_UPDATE_SEEK_BAR) {
                    updateSeekBar()
                    true
                } else {
                    false
                }
            }
            val content = models[adapterPosition]

            binding.btnPlayPause.setOnClickListener {
                if (adapterPosition == playingAudioPos) {
                    if (player?.isPlaying == true) {
                        player?.pause()
                        uiUpdateHandler?.removeMessages(MSG_UPDATE_SEEK_BAR)
                        binding.btnPlayPause.setImageResource(R.drawable.ic_play)
                    } else {
                        player?.start()
                        uiUpdateHandler?.sendEmptyMessageDelayed(MSG_UPDATE_SEEK_BAR, 1000)
                        binding.btnPlayPause.setImageResource(R.drawable.ic_pause)
                    }
                } else {
                    playingAudioPos = adapterPosition
                    if (player == null) {
                        if (this@TopicOverviewAdapter.playingHolder != null) {
                            updateToNonPlayingView(playingHolder)
                        }
                        player?.release()

                        val audioUrl = content.body.toString().dropLast(4)
                        val resId = binding.root.context.resources.getIdentifier(
                            audioUrl,
                            "raw",
                            binding.root.context.packageName
                        )

                        player =
                            MediaPlayer.create(binding.root.context, resId).also {
                                binding.sbController.isEnabled = true
                                binding.tvFullTime.text =
                                    "/ ${it.duration.toLong().milliSecondsToTimer()}"
                                binding.sbController.max = it.duration

                                it.setOnErrorListener { mediaPlayer, i, i2 ->
                                    mediaPlayer.stop()
                                    true
                                }

                                it.setOnCompletionListener {
                                    binding.tvCurrentTime.text = "00:00"
                                    binding.sbController.progress = 0
                                }
                                binding.btnPlayPause.setImageResource(R.drawable.ic_pause)
                                this@TopicOverviewAdapter.playingHolder = this
                                it.start()
                                uiUpdateHandler?.sendEmptyMessageDelayed(MSG_UPDATE_SEEK_BAR, 1000)
                            }

                    }
                }

                binding.sbController.setOnSeekBarChangeListener(object :
                    SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(p0: SeekBar?, p1: Int, fromUser: Boolean) {
                        if (fromUser) {
                            binding.tvCurrentTime.text = p1.toLong().milliSecondsToTimer()
                            player?.seekTo(p1)
                        }
                    }

                    override fun onStartTrackingTouch(p0: SeekBar?) {
                        player?.pause()
                        binding.btnPlayPause.setImageResource(R.drawable.ic_play)
                    }

                    override fun onStopTrackingTouch(p0: SeekBar?) {
                    }

                })
            }
        }
    }


    inner class TextViewHolder(private val binding: ItemTextBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val content = models[adapterPosition]
            binding.tvText.text = Html.fromHtml(content.body.toString(), Html.FROM_HTML_MODE_LEGACY)
        }
    }

    inner class TextHeadlineViewHolder(private val binding: ItemHeadlineBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val content = models[adapterPosition]
            binding.tvText.text = Html.fromHtml(content.body.toString(), Html.FROM_HTML_MODE_LEGACY)
        }
    }

    inner class EmptyViewHolder(private val binding: ItemEmptyBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class ImageViewHolder(private val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val content = models[adapterPosition]
            val resourceId = binding.root.context.getResourceId(content.body.toString())
            binding.ivPic.setImageResource(resourceId)
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (models[position].type) {
            "Image" -> (holder as ImageViewHolder).bind()
            "Normal" -> (holder as TextHeadlineViewHolder).bind()
            "Transcript" -> (holder as TextViewHolder).bind()
            "Example" -> (holder as TextViewHolder).bind()
            "Tips" -> (holder as TextViewHolder).bind()
            "Audio" -> (holder as AudioViewHolder).bind()
            "Video" -> (holder as TextViewHolder).bind()
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        if (playingAudioPos == holder.adapterPosition) {
            updateToNonPlayingView(holder as AudioViewHolder)
            playingHolder = null
        }
    }

    override fun getItemViewType(position: Int): Int {
        models[position].let {
            return when (models[position].type) {
                "Image" -> IMAGE
                "Normal" -> HEADLINE
                "Transcript" -> TEXT
                "Example" -> TEXT
                "Tips" -> TEXT
                "Audio" -> AUDIO
                "video" -> VIDEO
                else -> -1
            }
        }
    }

    override fun getItemCount() = models.size

    var models = listOf<Content>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


//    fun submitList(models: List<MaterialGenericData>, lf: Lifecycle) {
//        this.models = models
//        this.lifecycle = lf
//        this.uiUpdateHandler = Handler(Looper.getMainLooper(), this)
//    }


    private fun updateToNonPlayingView(holder: AudioViewHolder?) {
        if (holder != null) {
            uiUpdateHandler?.removeMessages(MSG_UPDATE_SEEK_BAR)
            val playBtn = holder.itemView.findViewById<AppCompatImageButton>(R.id.btn_play_pause)
            playBtn.setImageResource(R.drawable.ic_play)
            val seekBar = holder.itemView.findViewById<SeekBar>(R.id.sb_controller)
            holder.itemView.findViewById<TextView>(R.id.tv_current_time).text = "00:00"
            seekBar.isEnabled = false
            seekBar.progress = 0
        }
    }

    fun releaseMediaPlayer() {
        if (playingHolder != null) {
            updateToNonPlayingView(playingHolder)
        }
        player?.let {
            it.release()
            player = null
            playingAudioPos = -1
        }
    }

    //
    override fun handleMessage(msg: Message): Boolean {
        when (msg.what) {
            MSG_UPDATE_SEEK_BAR -> {
                playingHolder?.itemView?.let {
                    it.findViewById<TextView>(R.id.tv_current_time).text =
                        player?.currentPosition?.toLong()?.milliSecondsToTimer()
                    it.findViewById<SeekBar>(R.id.sb_controller).progress =
                        player?.currentPosition ?: 0
                    uiUpdateHandler?.sendEmptyMessageDelayed(MSG_UPDATE_SEEK_BAR, 1000)
                }
                return true
            }
        }
        return false
    }


}

