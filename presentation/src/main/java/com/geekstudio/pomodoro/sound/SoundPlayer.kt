package com.geekstudio.pomodoro.sound

import android.bluetooth.BluetoothAssignedNumbers
import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.media.*
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.annotation.RawRes
import com.geekstudio.pomodoro.R


/**
 * SoundPool를 이용한 간단한 소리 재생
 */
class SoundPlayer(private val context: Context) {
    /**
     * 응용프로그램의 오디오 리소스를 관리하고 재생합니다.
     * SoundPool은 APK 내의 리소스 또는 파일 시스템의 파일에서 메모리로로드 할 수 있는 샘플 모음입니다. SoundPool 라이브러리는 MediaPlayer 서비스를 이용하여 오디오를 원시 16비트 PCM 모노 또는 스테리오 스트림으로 디코딩합니다.
     * 이를 통해 애플리케이션은 재생 중에 CPU 로드 및 압축 해제 지연 시간을 겪지 않고도 압축된 스트림을 제공할 수 있습니다.
     */
    private var soundPool: SoundPool? = null

    /**
     * 최소한의 설정으로 오디오와 동영상을 모두 가져오고 디코딩하며 재생할 수 있습니다. (로컬, 내부 URI, 외부 URL)
     * 인터넷 권한 : MediaPlayer를 사용하여 네트워크 기반 콘텐츠를 스트리밍하는 경우 애플리케이션에서 네트워크 액세스를 요청해야합니다. (android.permission.INTERNET 권한 필요)
     * Wake Lock 권한 : 플레이어 애플리케이션에서 화면이 어두워지는 것이나 프로세서의 절전 모드를 방지해야 한다면 또는 MediaPlayer.setScreenOnWhilePlaying 이나 MediaPlayer.setWakeMode 메서드를 사용한다면 이 권한을 요청해야합니다.
     */
    private var mediaPlayer: MediaPlayer? = null

    /**
     * 벨소리, 알림 또는 기타 유사한 유형의 사운드를 재생하는 빠른 방법을 제공합니다. Ringtone의 지정된 위치에 있는 벨소리를 가져옵니다.
     */
    private var ringtone: Ringtone? = null

    private var soundUri: Uri? = null
    private var streamId: Int? = null

    private var vibrator: Vibrator? = null
    private var audioManager: AudioManager? = null

    private var isInitSoundPool = false
    private var isInitMediaPlayer = false
    private var isInitRingtoneManger = false

    var isLoop = true
    var isPlay = false
    var isPrepare = false
    var vibrate: LongArray? = DEFAULT_VIBRATE
    var mode: Int = AudioManager.RINGER_MODE_NORMAL

    fun init() {
        if (!isInitMediaPlayer)
            initMediaPlayer()

        if (!isInitSoundPool)
            initSoundPool()

        if (!isInitRingtoneManger)
            initRingtoneManger()
    }

    private fun initMediaPlayer() {
        mediaPlayer = MediaPlayer.create(context, SOUND_ID)

        //미디어 소스가 재생 준비가되었을 때 호출 될 콜백에 대한 인터페이스 정의입니다.
        mediaPlayer?.setOnPreparedListener {
            Log.d(TAG, "MediaPlayer OnPreparedListener start!")
            isPrepare = true

            if (isPlay && mode == AudioManager.RINGER_MODE_NORMAL)
                mediaPlayer!!.start()
        }

        /*//미디어 소스 재생이 완료되었을 때 호출 할 콜백에 대한 인터페이스 정의입니다.
        mediaPlayer?.setOnCompletionListener {
            Log.d(TAG, "MediaPlayer OnCompletionListener start!")
            it.start()
        }

        //비동기 작업 중에 오류가 발생했을 때 호출 될 콜백의 인터페이스 정의입니다 (다른 오류는 메서드 호출시 예외를 발생시킵니다).
        mediaPlayer?.setOnErrorListener { mp, what, extra ->
            Log.d(TAG, "MediaPlayer OnErrorListener start!")
            isPrepare = false
            isPlay = false
            return@setOnErrorListener false
        }*/

        mediaPlayer?.isLooping = isLoop
        isInitMediaPlayer = true
    }

    private fun initRingtoneManger() {
        val packageName = context.packageName
        val uriPath = "android.resource://$packageName/$SOUND_ID"
        Log.d(TAG, "UriPath = $uriPath")
        soundUri = Uri.parse(uriPath)
        ringtone = RingtoneManager.getRingtone(context, soundUri)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ringtone?.isLooping = isLoop
        }

        isInitRingtoneManger = true
    }

    /**
     * SoundPool 초기화
     *  LoadCompleteListener에서 streamId는 성공하면 0이 아닌 값 반환하고 실패하면 0 반환
     */
    private fun initSoundPool() {
        soundPool = SoundPool.Builder().setAudioAttributes(getDefaultAudioAttributes()).build()
        soundPool?.setOnLoadCompleteListener { soundPool, sampleId, status ->
            when (status) {
                STATUS_PLAY_SOUND_SUCCESS -> {
                    Log.d(TAG, "Load Sound Success! SampleId = $sampleId")
                    streamId = soundPool.play(sampleId, 1f, 1f, 0, if (isLoop) -1 else 0, 1f)
                }
                else -> {
                    Log.d(TAG, "Load Sound Fail")
                }
            }
        }

        isInitSoundPool = true
    }

    private fun getDefaultAudioAttributes(): AudioAttributes = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_GAME)
        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
        .build()

    enum class PlayType {
        SoundPool, MediaPlayer, RingtoneManager
    }

    /**
     * RingerMode과 PlayType에 따른 사운드 정지한다
     */
    fun stop(playType: PlayType) {
        isPlay = false
        isPrepare = false

        mode = getPhoneRingerMode()
        Log.d(TAG, "stop mode = $mode, type = ${playType.name}")

        when (mode) {
            // 진동 모드
            AudioManager.RINGER_MODE_VIBRATE -> stopVibrate()
            // 소리 모드
            AudioManager.RINGER_MODE_NORMAL -> stopNormal(playType)
            // 무음 모드
            AudioManager.RINGER_MODE_SILENT -> {
                stopVibrate()
                stopNormal(playType)
            }
        }
    }

    private fun stopNormal(playType: PlayType) {
        when (playType) {
            PlayType.SoundPool -> {
                streamId?.run {
                    soundPool?.stop(this)
                }
            }

            PlayType.MediaPlayer -> {
                mediaPlayer?.stop()
            }

            PlayType.RingtoneManager -> {
                ringtone?.stop()
            }
        }
    }

    private fun stopVibrate() {
        if (vibrator != null)
            vibrator!!.cancel()
    }

    /**
     * RingerMode과 PlayType에 따른 사운드 재생한다
     */
    fun play(playType: PlayType) {
        isPlay = true

        if (!isInitRingtoneManger || !isInitSoundPool || !isInitMediaPlayer)
            throw NotInitSoundPlayerException()

        mode = getPhoneRingerMode()
        Log.d(TAG, "play mode = $mode, type = ${playType.name}")

        when (mode) {
            // 진동 모드
            AudioManager.RINGER_MODE_VIBRATE -> startVibrate()
            // 소리 모드
            AudioManager.RINGER_MODE_NORMAL -> startNormal(playType)
            // 무음 모드
            AudioManager.RINGER_MODE_SILENT -> {
                startVibrate()
                startNormal(playType)
            }
        }
    }

    /**
     * 진동 실행
     */
    private fun startVibrate() {
        if (vibrator == null)
            vibrator = context.getSystemService(VIBRATOR_SERVICE) as Vibrator

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val effect = VibrationEffect.createWaveform(vibrate, if (isLoop) 0 else -1)
            vibrator!!.vibrate(effect)
        } else {
            vibrator!!.vibrate(vibrate, -1)
        }
    }

    /**
     * PlayType에 따른 벨소리 실행
     */
    private fun startNormal(playType: PlayType) {
        when (playType) {
            PlayType.MediaPlayer -> {
                val isPlaying = mediaPlayer?.isPlaying

                if (isPlaying!!) {
                    Log.d(TAG, "MediaPlayer isPlaying = $isPlaying, Restart play()")

                    mediaPlayer?.stop()
                }

                mediaPlayer?.start()

            }

            PlayType.SoundPool -> {
                val soundId = soundPool?.load(context, SOUND_ID, 1)
                Log.d(TAG, "SoundPool play() soundId = $soundId")
            }

            PlayType.RingtoneManager -> {
                val isPlaying = ringtone?.isPlaying

                if (isPlaying!!) {
                    Log.d(TAG, "RingtoneManager isPlaying = $isPlaying, Restart play()")
                    ringtone?.stop()
                }

                ringtone?.play()
            }
        }
    }

    /**
     * @return
     * - 진동 모드 : AudioManager.RINGER_MODE_VIBRATE
     * - 벨소리 모드 : AudioManager.RINGER_MODE_NORMAL
     * - 무음 모드 : AudioManager.RINGER_MODE_SILENT
     */
    private fun getPhoneRingerMode(): Int {
        if (audioManager == null)
            audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        return audioManager!!.ringerMode
    }


    /**
     *  - SoundPool : release() 호풀하여 사용중인 모든 기본 리소스를 해제한 다음 SoundPool 참조를 Null로 설정해야합니다. 새로운 시작하면 새 SoundPoll이 생성디ㅗ고 사운드가 로드되고 재생이 다시 시작되어야합니다.
     *
     *  - MediaPlayer : MediaPlayer 개체가 더 이상 사용되지 않으면 release() 즉시 호출하여 MediaPlayer 개체와 연결된 내부 플레이어 엔진에서 사용하는 리소스가 즉시 해제될 수 있도록 하는 것이 좋습니다.
     * 리소스에는 하드웨어 가속 구성 요소와 같은 단일 리소스가 포함될 수 있으며 release()를 호출하지 않으면 MediaPlayer 개체의 후속 인스턴스가 소프트웨어 구현으로 대체되거나 완전히 실패할 수 있습니다.
     *
     *  - RingtoneManager :
     */
    fun clear() {
        stopVibrate()

        mediaPlayer?.release()
        mediaPlayer = null
        isInitMediaPlayer = false

        soundPool?.release()
        soundPool = null
        isInitSoundPool = false

        ringtone?.stop()
        isInitRingtoneManger = false
    }

    class NotInitSoundPlayerException: Exception("Not Init SoundPlayer")

    companion object {
        const val TAG = "SoundPlayer"

        //        var SOUND_ID: Int? = null
        @RawRes
        const val SOUND_ID = R.raw.sound
        var DEFAULT_VIBRATE:LongArray? = longArrayOf(0L, 1000L, 500L, 1000L)
        const val STATUS_PLAY_SOUND_SUCCESS = 0
    }
}