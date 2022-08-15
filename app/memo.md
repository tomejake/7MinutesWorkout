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