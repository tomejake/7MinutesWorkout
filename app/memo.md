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