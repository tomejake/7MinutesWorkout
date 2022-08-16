## 뷰 바인딩
1. build.gradle (Module) 에서 바인딩을 사용 하겠다고 설정
buildFeatures{
    viewBinding true
}

2. 사용하려는 Activity 에서 선언과 활용

3. 메모리 누락 발생을 없애기 위해 항상 onDestroy 를 만들어야 함

``` kotlin

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // 따로 id 를 찾아줄 필요 없이 바로 binding 을 통해 접근가능!
        // flStart 는 activity_main 에 있는 item 의 id임
        binding?.flStart?.setOnClickListener{

        }
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }
}

```

## 툴바
### 툴바 UI
<androidx.appcompat.widget.Toolbar
        android:id="@+id/toolbarExercise"
        android:theme="@style/ToolbarTheme"
        android:layout_width="match_parent"
        android:layout_height="?android:attr/actionBarSize"
        android:background="@color/white"
        android:titleTextColor="@color/colorPrimary"/>
- theme 에서 actionBar 를 안보이게 설정했지만 다른 화면에선 보이기 위해 따로 만듬

### 툴바 구현 (+ 상단 bar 에서 뒤로가기 구현)
``` kotlin

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityExerciseBinding.inflate(layoutInflater)
    setContentView(binding?.root)

    // ActionBar 지정
    setSupportActionBar(binding?.toolbarExercise)

    // supportActionBar 가 존재한다면 뒤로가기 버튼이 보이도록 설정
    if(supportActionBar != null){
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    // 상단 툴바에 뒤로가기 버튼이 동작하게 해주는 함수
    binding?.toolbarExercise?.setNavigationOnClickListener {
        onBackPressed()
    }
}

```

## TTS
- 텍스트를 읽어주는 기능

``` kotlin

private var tts: TextToSpeech? = null

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding?.root)

    tts = TextToSpeech(this, this)

    binding?.btnSpeak.setOnClickListener {
        speakOut(binding?.etEnteredText?.text.toString())
    }
}

override fun onInit(status: Int){
    // TTS 가 성공했다면 언어설정
    if(status == TextToSpeech.SUCCESS){
        val result = tts!!.setLanguage(Locale.US)

        // TextToSpeech.LANG_MISSING_DATA: 언어 데이터 존재하지 않음
        // TextToSpeech.LANG_NOT_SUPPORTED: 지원하지 않는 언어
        if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
            // 지원 안한다는 로그 생성
            Log.e("TTS", "The Language specified is not supported!")
        }
    }
}

private fun speakOut(text: String){
    // 4번째 요소: utteranceId = 코멘트, 발언, 표현 문장 같은 것? 모국어가 아닌 사람에게 유용함
    tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
}

override fun onDestroy() {
    super.onDestroy()

    if(tts != null){
        tts?.stop()
        sst?.shutdown()
    }

    binding = null
}

```