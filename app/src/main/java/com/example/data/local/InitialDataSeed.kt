package com.example.data.local

import com.example.data.model.*

object InitialDataSeed {

    fun getDemoUsers(): List<User> = listOf(
        User(
            id = "demo_student_1",
            email = "student@uhvsadhana.demo",
            name = "Sample Student",
            rollNo = "25KB1A0000",
            role = UserRole.STUDENT,
            branch = "CSE",
            section = "A",
            year = "IV",
            mobile = "9876543210",
            xp = 350,
            streakDays = 5,
            isDemoAccount = true
        ),
        User(
            id = "demo_faculty_1",
            email = "faculty@uhvsadhana.demo",
            name = "Dr. K. S. Murthy",
            rollNo = "FAC-UHV-01",
            role = UserRole.FACULTY,
            branch = "CSE / UHV",
            section = "All",
            year = "All",
            xp = 900,
            streakDays = 12,
            isDemoAccount = true
        ),
        User(
            id = "demo_admin_1",
            email = "admin@uhvsadhana.demo",
            name = "UHV Cell Coordinator",
            rollNo = "ADM-UHV-01",
            role = UserRole.ADMIN,
            branch = "UHV Cell",
            section = "Admin",
            year = "Staff",
            xp = 1500,
            streakDays = 25,
            isDemoAccount = true
        ),
        User(
            id = "demo_superadmin_1",
            email = "superadmin@uhvsadhana.demo",
            name = "Principal / Director",
            rollNo = "DIR-NBKR-01",
            role = UserRole.SUPER_ADMIN,
            branch = "NBKRIST Administration",
            section = "Leadership",
            year = "Staff",
            xp = 2500,
            streakDays = 40,
            isDemoAccount = true
        ),
        // Additional mock students for leaderboard
        User(
            id = "student_2",
            email = "rithika.reddy@student.nbkrist.org",
            name = "Rithika Reddy",
            rollNo = "23KB1A0589",
            role = UserRole.STUDENT,
            branch = "CSE",
            section = "B",
            year = "III",
            xp = 480,
            streakDays = 7,
            isDemoAccount = false
        ),
        User(
            id = "student_3",
            email = "praveen.k@student.nbkrist.org",
            name = "Praveen Kumar",
            rollNo = "23KB1A0412",
            role = UserRole.STUDENT,
            branch = "ECE",
            section = "A",
            year = "III",
            xp = 420,
            streakDays = 6,
            isDemoAccount = false
        ),
        User(
            id = "student_4",
            email = "ananya.s@student.nbkrist.org",
            name = "Ananya Sharma",
            rollNo = "24KB1A0105",
            role = UserRole.STUDENT,
            branch = "EEE",
            section = "A",
            year = "II",
            xp = 390,
            streakDays = 4,
            isDemoAccount = false
        )
    )

    fun getModules(): List<UhvModule> = listOf(
        UhvModule(
            id = "mod_1",
            orderIndex = 1,
            titleEn = "Module 1: Introduction to Value Education & Self Exploration",
            titleTe = "మాడ్యూల్ 1: విలువల విద్య పరిచయం & స్వీయ అన్వేషణ",
            taglineEn = "Understanding What is Valuable for Human Being",
            taglineTe = "మానవ మనుగడకు అత్యంత విలువైనది ఏమిటో గ్రహించడం",
            description = "Covers the need, basic guidelines, content and process for value education, basic human aspirations: continuous happiness and prosperity, and the process of self-exploration.",
            lecturesCount = 6,
            practiceCount = 3,
            iconName = "explore",
            totalLessons = 4,
            isUnlocked = true
        ),
        UhvModule(
            id = "mod_2",
            orderIndex = 2,
            titleEn = "Module 2: Harmony in the Human Being",
            titleTe = "మాడ్యూల్ 2: మానవునిలో సామరస్యం",
            taglineEn = "Co-existence of Self ('I') and Body",
            taglineTe = "నేను ('I') మరియు శరీరం యొక్క సహ-అస్తిత్వం",
            description = "Exploration of the human being as a co-existence of the conscious self ('I') and material 'Body'. Understanding needs, activities, and harmony in the Self.",
            lecturesCount = 6,
            practiceCount = 3,
            iconName = "self_improvement",
            totalLessons = 4,
            isUnlocked = true
        ),
        UhvModule(
            id = "mod_3",
            orderIndex = 3,
            titleEn = "Module 3: Harmony in Family & Society",
            titleTe = "మాడ్యూల్ 3: కుటుంబం & సమాజంలో సామరస్యం",
            taglineEn = "Trust, Respect & Justice in Human Relationships",
            taglineTe = "మానవ సంబంధాలలో నమ్మకం, గౌరవం మరియు న్యాయం",
            description = "Understanding values in human-human relationships: Trust (Vishwas) as foundational value, Respect (Samman) as right evaluation, and the comprehensive human goal in society.",
            lecturesCount = 6,
            practiceCount = 3,
            iconName = "groups",
            totalLessons = 4,
            isUnlocked = true
        ),
        UhvModule(
            id = "mod_4",
            orderIndex = 4,
            titleEn = "Module 4: Harmony in Nature & Existence",
            titleTe = "మాడ్యూల్ 4: ప్రకృతి & ఉనికిలో సామరస్యం",
            taglineEn = "Interconnectedness and Mutual Fulfillment",
            taglineTe = "పరస్పర అనుసంధానం మరియు పరస్పర పరిపూర్ణత",
            description = "Understanding the four orders in Nature: Material, Bio/Pranic, Animal, and Human orders. Recyclability, self-regulation, and co-existence in the whole existence.",
            lecturesCount = 6,
            practiceCount = 3,
            iconName = "eco",
            totalLessons = 3,
            isUnlocked = true
        ),
        UhvModule(
            id = "mod_5",
            orderIndex = 5,
            titleEn = "Module 5: Holistic Understanding & Professional Ethics",
            titleTe = "మాడ్యూల్ 5: సంపూర్ణ అవగాహన & వృత్తిపరమైన నీతి",
            taglineEn = "Universal Human Order and Ethical Conduct",
            taglineTe = "సార్వత్రిక మానవ క్రమం మరియు నైతిక ప్రవర్తన",
            description = "Natural acceptance of human values, competence in professional ethics, holistic technologies, production systems, and management models. Transition towards universal human order.",
            lecturesCount = 4,
            practiceCount = 2,
            iconName = "verified_user",
            totalLessons = 3,
            isUnlocked = true
        )
    )

    fun getLessons(): List<UhvLesson> = listOf(
        // Module 1 Lessons
        UhvLesson(
            id = "les_1_1",
            moduleId = "mod_1",
            orderIndex = 1,
            titleEn = "Need, Basic Guidelines & Content of Value Education",
            titleTe = "విలువల విద్య యొక్క ఆవశ్యకత, మార్గదర్శకాలు మరియు విషయ సూచిక",
            description = "Why education must focus on both 'what to do' (value domain) and 'how to do' (skill domain).",
            learningObjectives = "Distinguish between Value Education and Skill Education\nIdentify the basic guidelines: Universal, Rational, Verifiable, Leading to Harmony\nRecognize why technology without human values leads to ecological and social crises",
            mainContent = """Education must fulfill two vital dimensions: the value domain, which clarifies what is truly valuable and worth pursuing for human fulfillment, and the skill domain, which equips us with the scientific and technological competence to materialize those goals. While contemporary engineering and technical education excels in skill enhancement, a critical void remains in discerning what leads to authentic, sustainable human happiness.

Universal Human Values education provides guidelines that are universal (applying to all humans regardless of nationality, creed, or time), rational (amenable to logical inquiry), natural and verifiable (verifiable on the basis of natural acceptance in oneself), and leading to harmony at all four levels: individual, family, society, and nature. Without values, skills become tools for exploitation and environmental degradation; with values, technical competence transforms into service for universal welfare.""",
            keyTakeaways = "Values determine 'what to do'; skills provide 'how to do'.\nValue education must be universal, rational, natural, and verifiable.\nHarmony is the ultimate outcome at all four levels of living.\nRight understanding is the foundation for right living.",
            campusExample = "Consider engineering students developing an automated industrial system. Technical skills ensure high throughput, but value orientation determines worker safety, fair compensation, and zero toxic effluent release into local water bodies.",
            reflectionQuestion = "Recall a recent choice you made. Was it guided primarily by external peer pressure/marketing or by your own natural acceptance of what brings lasting harmony?",
            durationMinutes = 15,
            videoTitle = "Lecture 1: Purpose of Value Education (AICTE)",
            videoUrl = "https://www.youtube.com/watch?v=sample_uhv1",
            isCompleted = true
        ),
        UhvLesson(
            id = "les_1_2",
            moduleId = "mod_1",
            orderIndex = 2,
            titleEn = "Basic Human Aspirations: Happiness & Prosperity",
            titleTe = "ప్రాథమిక మానవ ఆకాంక్షలు: ఆనందం మరియు శ్రేయస్సు",
            description = "Exploring the continuity of happiness and the feeling of having more physical facility than required.",
            learningObjectives = "Define Happiness as living in a state of harmony\nDefine Prosperity as the feeling of having more than required physical facilities\nRecognize the crucial difference between wealth and prosperity",
            mainContent = """At the root of all human endeavors lies the fundamental aspiration for continuous happiness and prosperity. Happiness is not merely transient sensory pleasure or excitement; it is the state of being in harmony within oneself and with the surroundings. To be in harmony is happiness; to be in contradiction or conflict is unhappiness.

Prosperity is the feeling of having, or producing, more physical facility than is required for the nurturing, protection, and right utilization of the body. An essential realization is that 'wealth' is physical, measurable in currency or goods, whereas 'prosperity' is a subjective feeling. A person with accumulating hoardings of wealth may continuously suffer from the acute fear of deprivation and greed, living in poverty of mind, whereas one who accurately assesses bodily needs and produces abundantly experiences true prosperity.""",
            keyTakeaways = "Continuous happiness is the natural state of internal harmony.\nProsperity is a feeling of sufficiency, distinct from endless accumulation.\nPhysical facilities are necessary, but insufficient alone for fulfillment.\nRight understanding + Right relationship + Physical facilities = Complete fulfillment.",
            campusExample = "A student competing aggressively for status gadgets feels perpetual envy, while another student who shares study materials and feels content with adequate tools feels genuine joy and prosperity.",
            reflectionQuestion = "Have you ever felt dissatisfied despite possessing all necessary physical items? What was missing in that moment?",
            durationMinutes = 18,
            videoTitle = "Lecture 2: Continuous Happiness & Prosperity",
            videoUrl = "https://www.youtube.com/watch?v=sample_uhv2",
            isCompleted = true
        ),
        UhvLesson(
            id = "les_1_3",
            moduleId = "mod_1",
            orderIndex = 3,
            titleEn = "Process of Self-Exploration & Natural Acceptance",
            titleTe = "స్వీయ అన్వేషణ ప్రక్రియ & సహజ స్వీకృతి",
            description = "Understanding how to dialogue within oneself to discover truth without dogmatic beliefs.",
            learningObjectives = "Understand Self-Exploration as a process of observing within\nDistinguish Natural Acceptance from conditioned assumptions and sensations\nValidate proposals through personal experiential validation",
            mainContent = """Self-exploration is an observational dialogue between 'what I am' and 'what is naturally acceptable to me'. Rather than accepting teachings on blind faith or external authority, every concept is received as a proposal. The learner verifies each proposal through Natural Acceptance (Sahaj Svikriti)—the invariant, innate human sense of truth that desires harmony, trust, respect, and mutual enrichment.

Natural Acceptance does not alter with time, geographical location, age, or culture. For instance, whether in ancient times or the 21st century, every person naturally desires to be trusted rather than mistrusted, respected rather than insulted. By experiential validation—living according to the proposal and testing mutual fulfillment with human beings and mutual enrichment with nature—one attains authentic certainty.""",
            keyTakeaways = "All teachings in UHV are proposals, not dogmas.\nNatural Acceptance is invariant, uncorrupted by conditioning.\nExperiential validation leads to mutual happiness and mutual prosperity.\nSelf-exploration closes the gap between intention and competence.",
            campusExample = "When facing ragging or peer mockery, conditioned habits might prompt defensiveness, but natural acceptance clearly affirms that treating a junior with kindness and respect feels inherently wholesome.",
            reflectionQuestion = "Is your desire to be respected dependent on what peers say, or is it an innate, constant expectation inside you?",
            durationMinutes = 20,
            videoTitle = "Lecture 3: Self Exploration & Experiential Validation",
            videoUrl = "https://www.youtube.com/watch?v=sample_uhv3",
            isCompleted = false
        ),
        UhvLesson(
            id = "les_1_4",
            moduleId = "mod_1",
            orderIndex = 4,
            titleEn = "Current Scenario: Happiness from Sensation & Preconditioning",
            titleTe = "ప్రస్తుత పరిస్థితి: భావోద్వేగాలు మరియు పూర్వ భావనల నుండి ఆనందం",
            description = "Critique of consumerism and seeking permanent happiness through temporary physical sensations.",
            learningObjectives = "Identify the instability of sensation-based pleasure\nAnalyze the pitfalls of social preconditioning\nFormulate the transition towards harmony based on Right Understanding",
            mainContent = """Modern society predominantly attempts to extract continuous happiness through two flawed avenues: sensory gratification (taste, touch, sight, sound, smell) and social preconditioning (status symbols, brand validation, titles). However, sensory pleasure is inherently transient; food tastes delicious when hungry, but turns repulsive when overconsumed. Sensation cannot provide continuity of happiness.

Similarly, preconditioning—borrowed beliefs without self-verification—leads to endless competition, anxiety, and interpersonal friction. When happiness is pegged to outperforming others or possessing rare luxuries, conflict becomes inevitable. True stability emerges only when happiness is anchored in Right Understanding within the Self, nurturing harmonious relationships in family and society.""",
            keyTakeaways = "Sensory pleasure is temporary and cannot yield continuous happiness.\nPreconditioning creates artificial dependencies and peer conflict.\nRight understanding is the only perpetual anchor for well-being.\nPhysical facilities serve bodily nurture, not ego gratification.",
            campusExample = "Buying an expensive smartphone brings excitement for a week, after which the device becomes routine, and anxiety sets in when a peer flaunts an upgraded edition.",
            reflectionQuestion = "List three things you purchased recently. Did they bring enduring peace or momentary thrill?",
            durationMinutes = 15,
            videoTitle = "Lecture 4: Understanding Illusions of Happiness",
            videoUrl = "https://www.youtube.com/watch?v=sample_uhv4",
            isCompleted = false
        ),

        // Module 2 Lessons
        UhvLesson(
            id = "les_2_1",
            moduleId = "mod_2",
            orderIndex = 1,
            titleEn = "Understanding the Human Being: Co-existence of Self ('I') and Body",
            titleTe = "మానవుని అవగాహన: 'నేను' మరియు శరీరం యొక్క సహ-అస్తిత్వం",
            description = "Distinguishing between the conscious Self that feels and decides, and the material Body as an instrument.",
            learningObjectives = "Distinguish the needs of the Self ('I') from the needs of the Body\nIdentify the activities of the Self: Desiring, Thinking, Expecting\nRecognize the Body as an instrument of the Self",
            mainContent = """A human being is fundamentally a co-existence of two distinct entities: the conscious entity designated as the Self or 'I' (Jeevan), and the physical/material entity known as the Body. The needs of the Body are physical and quantitative—food, clothing, shelter—which are needed in limited quantities for physiological maintenance. In stark contrast, the needs of the Self are qualitative and continuous—trust, respect, affection, peace, and happiness.

The Self is the seer (Drashta), the doer (Karta), and the enjoyer (Bhokta). The eyes do not see; 'I' see through the eyes. The mouth does not eat; 'I' decide to feed the body through the mouth. Confusing the needs of the Self with the needs of the Body is the principal cause of rampant materialism, where individuals attempt to cure emotional distress with physical purchases.""",
            keyTakeaways = "Human = Self ('I') + Body in mutual co-existence.\nNeeds of 'I' are qualitative & continuous (Respect, Trust).\nNeeds of Body are quantitative & temporary (Food, Medicine).\nSelf is the conscious decision maker; Body is the material instrument.",
            campusExample = "When an engineer sits coding late at night, the body feels fatigue and hunger (bodily need), while the Self experiences intellectual excitement and dedication to solving the algorithm.",
            reflectionQuestion = "When someone speaks harshly to you, does the sound wave physically injure your ear tissues, or does the conscious Self feel hurt due to lack of respect?",
            durationMinutes = 18,
            videoTitle = "Lecture 7: Co-existence of I and Body",
            videoUrl = "https://www.youtube.com/watch?v=sample_uhv7",
            isCompleted = false
        ),
        UhvLesson(
            id = "les_2_2",
            moduleId = "mod_2",
            orderIndex = 2,
            titleEn = "Harmony in the Self ('I'): Desire, Thought and Expectation",
            titleTe = "స్వీయంలో సామరస్యం: కోరిక, ఆలోచన మరియు ఆశ",
            description = "Analyzing the internal faculties of Ichha, Vichar, and Asha and their alignment with Natural Acceptance.",
            learningObjectives = "Map the internal activities of Imagination: Desire, Thought, Expectation\nUnderstand internal contradiction as the root of stress\nAchieve internal harmony through alignment with Natural Acceptance",
            mainContent = """Within the Self ('I'), activities are perpetually active in the form of Imagination (Kalpana): Desires (Ichha - goals/aims), Thoughts (Vichar - reasoning on how to achieve them), and Expectations (Asha - selecting sensory inputs and actions). When these three faculties are guided by preconditioned assumptions or unexamined sensations, contradictions emerge. A student might desire genuine mastery, think about shortcut cheat-sheets, and expect praise from teachers. This internal misalignment causes acute anxiety and mental exhaustion.

When Desires are anchored directly in Natural Acceptance, Thoughts naturally develop win-win pathways, and Expectations align with mutual fulfillment. The state of self-organization (Sanyam) within the Self produces calm clarity and emotional resilience.""",
            keyTakeaways = "Imagination comprises Desires, Thoughts, and Expectations.\nContradiction between Desires and Natural Acceptance creates internal stress.\nAlignment of Imagination with Natural Acceptance produces unshakeable peace.\nSelf-awareness is the continuous observation of one's thoughts and motives.",
            campusExample = "Before an examination, a student who has reconciled study discipline with the desire for real competence remains peaceful, whereas one burdened by contradictory wishes suffers panic.",
            reflectionQuestion = "Observe your thoughts for five minutes. Are your current thoughts serving your authentic long-term aspiration or trivial transient distractions?",
            durationMinutes = 20,
            videoTitle = "Lecture 8: Harmony in the Self",
            videoUrl = "https://www.youtube.com/watch?v=sample_uhv8",
            isCompleted = false
        ),

        // Module 3 Lessons
        UhvLesson(
            id = "les_3_1",
            moduleId = "mod_3",
            orderIndex = 1,
            titleEn = "Harmony in the Family: Trust (Vishwas) as the Foundational Value",
            titleTe = "కుటుంబంలో సామరస్యం: ప్రాథమిక విలువగా నమ్మకం (విశ్వాస్)",
            description = "Differentiating between Intention (natural acceptance) and Competence (ability) to eliminate interpersonal mistrust.",
            learningObjectives = "Define Trust as the assurance that the other person intends my happiness\nDistinguish Intention from Competence\nEradicate complaints and cultivate unconditional trust in relationships",
            mainContent = """Relationship exists naturally between one Self ('I') and another Self ('I'). The foundational value of any relationship is Trust (Vishwas). Trust is the absolute assurance that the other person naturally desires my happiness and prosperity—their Intention is benevolent.

A universal tragedy in human interaction is that we judge ourselves by our Intention ('I meant well, but I lacked time or skill'), whereas we judge others by their Competence ('They made a mistake, so they must be malicious!'). When we clearly distinguish Intention from Competence, anger evaporates. We realize that mistakes are deficits of competence, not hostile intentions. Our response shifts from blaming and punishment to mutual understanding, patient communication, and helping the other build competence.""",
            keyTakeaways = "Trust is the foundational feeling in relationships.\nIntention is naturally benevolent in every human being.\nLack of ability is a competence deficit, not bad intention.\nJudging others by competence creates anger; recognizing intention fosters empathy.",
            campusExample = "In a lab project, when your teammate fails to submit their section on time, assuming bad intention sparks a shouting match. Inquiring about their difficulty and helping them solve it preserves both the project and the friendship.",
            reflectionQuestion = "Think of someone you recently felt angry with. Did they intentionally wake up wanting to harm you, or were they lacking competence in that situation?",
            durationMinutes = 22,
            videoTitle = "Lecture 13: Trust - Foundational Value in Relationships",
            videoUrl = "https://www.youtube.com/watch?v=sample_uhv13",
            isCompleted = false
        ),
        UhvLesson(
            id = "les_3_2",
            moduleId = "mod_3",
            orderIndex = 2,
            titleEn = "Respect (Samman) as Right Evaluation",
            titleTe = "గౌరవం (సమ్మాన్): సరైన మూల్యాంకనం",
            description = "Overcoming discrimination based on caste, gender, age, wealth, and skills by recognizing intrinsic human equality.",
            learningObjectives = "Define Respect as Right Evaluation (Samman = Samyak Maan)\nIdentify the fallacies of Over-evaluation, Under-evaluation, and Otherwise-evaluation\nRecognize the purpose of differentiation versus complementary relationship",
            mainContent = """Respect literally translates to Right Evaluation (Samman = Samyak Maan). Whenever we evaluate a person accurately as they are, we express respect. In contrast, evaluating someone more than they are (over-evaluation), less than they are (under-evaluation), or completely unrelated to who they are (otherwise-evaluation) causes disrespect.

Modern society routinely confuses differentiation with respect. We evaluate people on the basis of bodily attributes (age, gender, race, physical appearance), physical facilities (wealth, luxury vehicles, branded attire), or beliefs (caste, sect, ideology). On the level of the Self ('I'), however, our purpose, natural acceptance, and capacity are fundamentally identical: we both seek continuous happiness and harmony. Differences exist only in our current levels of understanding and competence, which calls for complementary support, not discrimination or arrogance.""",
            keyTakeaways = "Respect means right evaluation of the other.\nOver-evaluation flattery and under-evaluation mockery are both disrespectful.\nHuman beings are equal at the level of the Self; differences lie only in competence.\nComplementarity replaces competition when respect is established.",
            campusExample = "Mocking a junior student who speaks hesitant English is blatant under-evaluation. Respect involves recognizing their intelligence and supporting their language fluency with dignity.",
            reflectionQuestion = "Do you unconsciously accord greater deference to affluent classmates compared to campus cleaning staff? What does that reveal about your criteria for respect?",
            durationMinutes = 18,
            videoTitle = "Lecture 14: Respect as Right Evaluation",
            videoUrl = "https://www.youtube.com/watch?v=sample_uhv14",
            isCompleted = false
        ),

        // Module 4 Lessons
        UhvLesson(
            id = "les_4_1",
            moduleId = "mod_4",
            orderIndex = 1,
            titleEn = "Harmony in Nature: The Four Orders & Mutual Fulfillment",
            titleTe = "ప్రకృతిలో సామరస్యం: నాలుగు క్రమాలు మరియు పరస్పర పరిపూర్ణత",
            description = "Exploring the Material, Pranic, Animal, and Human orders and restoring ecological balance.",
            learningObjectives = "Classify the four orders in Nature\nIdentify the self-regulation and cyclicity among the first three orders\nRecognize human failure in mutual fulfillment and how to correct it",
            mainContent = """Nature consists of four interconnected orders:
1. Material Order (Padartha Avastha): Soil, water, minerals, atmosphere. Characterized by composition and decomposition.
2. Pranic/Bio Order (Prana Avastha): Plants, trees, vegetation. Characterized by growth, respiration, and cellular life.
3. Animal Order (Jeeva Avastha): Animals and birds. Possessing both conscious will for survival and physiological body.
4. Human Order (Gyana Avastha): Human beings endowed with self-awareness, moral contemplation, and quest for right understanding.

In nature, the first three orders exist in marvelous mutual fulfillment and cyclic regeneration. Soil nurtures plants; plants generate oxygen and feed herbivores; animals disperse seeds and fertilize soil. Human beings, despite being the pinnacle of consciousness, have historically acted parasitically—polluting material water, deforesting pranic lungs, and driving species to extinction. When humans attain Right Understanding, our role shifts from exploitation to conscious stewardship, ensuring mutual enrichment (Parasparik Purakata).""",
            keyTakeaways = "Nature comprises Material, Pranic, Animal, and Human orders.\nThe first three orders are innately cyclic and mutually fulfilling.\nHuman disharmony stems from ignorance and greed, not inherent biological design.\nRight understanding enables humans to live in mutual enrichment with all orders.",
            campusExample = "At NBKRIST campus, rainwater harvesting, solar installations, and organic gardens demonstrate practical integration of human engineering with nature's cyclicity.",
            reflectionQuestion = "Does your daily lifestyle enrich or deplete the material and pranic orders around you?",
            durationMinutes = 17,
            videoTitle = "Lecture 19: Harmony in Nature & Four Orders",
            videoUrl = "https://www.youtube.com/watch?v=sample_uhv19",
            isCompleted = false
        ),

        // Module 5 Lessons
        UhvLesson(
            id = "les_5_1",
            moduleId = "mod_5",
            orderIndex = 1,
            titleEn = "Holistic Understanding of Harmony & Professional Ethics",
            titleTe = "సామరస్యం యొక్క సమగ్ర అవగాహన & వృత్తిపరమైన నీతి",
            description = "Transitioning from prevailing exploitative engineering paradigms to ethical, eco-friendly technologies.",
            learningObjectives = "Define Professional Ethics as competence in human values\nAnalyze the pitfalls of corrupt practices and corporate greed\nFormulate criteria for holistic technology and sustainable production",
            mainContent = """Professional ethics is not a collection of regulatory constraints or punitive compliance rules; it is the natural outcome of holistic understanding. When an engineer or technologist comprehends the interconnectedness of existence, ethical conduct becomes intrinsic and joyous. Exploitative behavior—such as programmed obsolescence, environmental dumping, or deceptive software algorithms—is recognized as self-destructive.

Holistic technology meets five criteria: it is eco-friendly (cyclic and non-polluting), human-friendly (safe, dignifying for workers, decentralized), resource-conserving (harnessing renewable energy), sustainable over generations, and promoting mutual prosperity. Technical brilliance without ethical commitment creates catastrophic weapons and algorithmic surveillance; when anchored in UHV, engineering serves universal human well-being (Sarve Bhavantu Sukhinah).""",
            keyTakeaways = "Ethical conduct flows naturally from right understanding, not forced compliance.\nHolistic technology must be cyclic, human-friendly, and socially enriching.\nProfessionals are trustees of societal welfare and environmental health.\nEngineering excellence achieves true meaning when serving universal harmony.",
            campusExample = "An IT engineer refusing to insert deceptive dark patterns that trap elderly users into unwanted subscriptions embodies professional ethical competence.",
            reflectionQuestion = "If asked by an employer to falsify emission data or compromise product safety for profit, how would you respond?",
            durationMinutes = 20,
            videoTitle = "Lecture 25: Professional Ethics & Universal Order",
            videoUrl = "https://www.youtube.com/watch?v=sample_uhv25",
            isCompleted = false
        )
    )

    fun getPracticeActivities(): List<PracticeActivity> = listOf(
        PracticeActivity(
            id = "act_ps1",
            moduleId = "mod_1",
            sessionNumber = 1,
            titleEn = "PS 1: Exploring Natural Acceptance vs Preconditioning",
            titleTe = "పి.ఎస్ 1: సహజ స్వీకృతి మరియు పూర్వభావనల అన్వేషణ",
            activityType = "Self-Observation",
            scenarioPrompt = "Reflect on five beliefs you hold about success, money, and relationships. Determine whether each belief is derived from Natural Acceptance or external social conditioning (movies, advertising, peer expectations).",
            guidedQuestions = "1. List the belief.\n2. When did you adopt it?\n3. Does it lead to continuous harmony when lived?\n4. What does your deep natural acceptance say?",
            expectedOutcome = "Clarity in separating intrinsic desires from borrowed conditioning."
        ),
        PracticeActivity(
            id = "act_ps2",
            moduleId = "mod_1",
            sessionNumber = 2,
            titleEn = "PS 2: Distinguishing Needs of Self ('I') and Body",
            titleTe = "పి.ఎస్ 2: 'నేను' మరియు శరీరం యొక్క అవసరాల విభజన",
            activityType = "Value-Mapping",
            scenarioPrompt = "Conduct an audit of your daily expenses and time allocation over the last week. Categorize each item into: Nurturing the Body, Protection of the Body, Right Utilization of the Body, or Pleasing the Ego/Self.",
            guidedQuestions = "1. What percentage was spent purely on bodily maintenance?\n2. What percentage was spent seeking respect from others?\n3. Could the desire for respect be fulfilled through genuine care rather than spending?",
            expectedOutcome = "Recognition that emotional needs cannot be satisfied by physical accumulation."
        ),
        PracticeActivity(
            id = "act_ps3",
            moduleId = "mod_2",
            sessionNumber = 3,
            titleEn = "PS 3: Observing the Activities in the Self ('I')",
            titleTe = "పి.ఎస్ 3: స్వీయంలోని కార్యకలాపాల పరిశీలన",
            activityType = "Self-Observation",
            scenarioPrompt = "Spend 15 minutes in silence observing the continuous flow of desires, thoughts, and expectations occurring inside you without judging or suppressing them.",
            guidedQuestions = "1. What was the dominant thought theme?\n2. Were your thoughts in conflict or in mutual harmony?\n3. Did you notice the observer ('I') distinct from the thoughts?",
            expectedOutcome = "Cultivation of the witness consciousness and inner tranquility."
        ),
        PracticeActivity(
            id = "act_ps4",
            moduleId = "mod_3",
            sessionNumber = 4,
            titleEn = "PS 4: Distinguishing Intention from Competence in Trust",
            titleTe = "పి.ఎస్ 4: నమ్మకంలో ఉద్దేశం మరియు సామర్థ్యం మధ్య తేడా",
            activityType = "Campus Scenario",
            scenarioPrompt = "Select a relationship (roommate, parent, or faculty member) where friction or mistrust currently exists. Re-evaluate their actions by giving them full benefit of benevolent intention while analyzing competence gaps.",
            guidedQuestions = "1. What was their original positive intention?\n2. What specific competence or situational constraint were they missing?\n3. How does your emotional state change when you assume their good intention?",
            expectedOutcome = "Immediate reduction in resentment and restoration of communication."
        ),
        PracticeActivity(
            id = "act_ps5",
            moduleId = "mod_4",
            sessionNumber = 5,
            titleEn = "PS 5: Observing Recyclability in Local Ecosystem",
            titleTe = "పి.ఎస్ 5: స్థానిక పర్యావరణ వ్యవస్థలో పునర్వినియోగ పరిశీలన",
            activityType = "Campus Activity",
            scenarioPrompt = "Walk around the NBKRIST campus or your neighborhood. Identify two examples of cyclic mutual fulfillment in nature (e.g., fallen leaves turning into leaf mulch) and two examples of human-induced non-cyclic waste.",
            guidedQuestions = "1. Describe the natural cyclic process observed.\n2. Describe the linear waste product and its lifespan.\n3. Propose an engineering intervention to close the loop on the non-cyclic waste.",
            expectedOutcome = "Appreciation of ecological balance and commitment to zero-waste practices."
        )
    )

    fun getQuestions(): List<QuizQuestion> = listOf(
        // Module 1 Questions
        QuizQuestion(
            id = "q_1_1",
            moduleId = "mod_1",
            questionEn = "What is the primary role of Value Education in engineering curriculum?",
            questionTe = "ఇంజనీరింగ్ పాఠ్యాంశాల్లో విలువల విద్య యొక్క ముఖ్య ఉద్దేశ్యం ఏమిటి?",
            optionsJson = "To provide moral preaching|To clarify 'what to do' while skills clarify 'how to do'|To replace technical subjects with literature|To ensure exam grades only",
            correctOptionIndex = 1,
            explanation = "Value education clarifies the domain of 'what to do' (human purpose and fulfillment), while skill education provides 'how to do' (technical competence).",
            difficulty = "EASY"
        ),
        QuizQuestion(
            id = "q_1_2",
            moduleId = "mod_1",
            questionEn = "Which of the following is an essential guideline for Universal Human Values?",
            questionTe = "సార్వత్రిక మానవ విలువల కోసం ఈ క్రింది వాటిలో సరైన మార్గదర్శకం ఏది?",
            optionsJson = "Specific to one religion|Based on fear of punishment|Universal, Rational, and Naturally Verifiable|Variable across geographical borders",
            correctOptionIndex = 2,
            explanation = "Value education proposals must be universal (applicable to all), rational, and naturally verifiable on the basis of natural acceptance.",
            difficulty = "EASY"
        ),
        QuizQuestion(
            id = "q_1_3",
            moduleId = "mod_1",
            questionEn = "How is 'Prosperity' defined in Universal Human Values?",
            questionTe = "సార్వత్రిక మానవ విలువలలో 'శ్రేయస్సు' ఎలా నిర్వచించబడింది?",
            optionsJson = "Possessing more wealth than all neighbors|The feeling of having or producing more physical facilities than required|Continuous sensory consumption|Having high bank balance without sharing",
            correctOptionIndex = 1,
            explanation = "Prosperity is a subjective feeling of abundance—having or producing more physical facility than is required for bodily nurturing and protection.",
            difficulty = "MEDIUM"
        ),
        QuizQuestion(
            id = "q_1_4",
            moduleId = "mod_1",
            questionEn = "What is 'Natural Acceptance' (Sahaj Svikriti)?",
            questionTe = "'సహజ స్వీకృతి' అంటే ఏమిటి?",
            optionsJson = "Opinions enforced by parents|The innate, invariant human sense of harmony and mutual fulfillment|Changing trends on social media|Subconscious biological reflexes",
            correctOptionIndex = 1,
            explanation = "Natural Acceptance is the invariant, innate faculty within every human being that naturally accepts harmony, trust, respect, and mutual welfare.",
            difficulty = "MEDIUM"
        ),
        QuizQuestion(
            id = "q_1_5",
            moduleId = "mod_1",
            questionEn = "Seeking continuous happiness solely through sensory gratification leads to:",
            questionTe = "భావోద్వేగాల ద్వారా మాత్రమే నిరంతర ఆనందాన్ని వెతకడం దేనికి దారితీస్తుంది?",
            optionsJson = "Perpetual inner peace|Instability and dependency because sensations are inherently temporary|Guaranteed prosperity|Spiritual enlightenment",
            correctOptionIndex = 1,
            explanation = "Sensations are transient and quickly turn into revulsion or boredom upon over-consumption, leading to instability and craving.",
            difficulty = "MEDIUM"
        ),

        // Module 2 Questions
        QuizQuestion(
            id = "q_2_1",
            moduleId = "mod_2",
            questionEn = "The human being is an integrated co-existence of:",
            questionTe = "మానవుడు వేటి సమగ్ర సహ-అస్తిత్వం?",
            optionsJson = "Brain and Spinal cord|Conscious Self ('I') and Material Body|Physical possessions and Job title|DNA and Cellular chemicals only",
            correctOptionIndex = 1,
            explanation = "A human being is fundamentally the co-existence of the conscious Self ('I') and the material, physiological Body.",
            difficulty = "EASY"
        ),
        QuizQuestion(
            id = "q_2_2",
            moduleId = "mod_2",
            questionEn = "Which of the following is a need of the conscious Self ('I')?",
            questionTe = "చేతన స్వీయం ('నేను') యొక్క అవసరం ఏది?",
            optionsJson = "Food and nutrients|Trust and Respect|Warm clothing| shelter from rain",
            correctOptionIndex = 1,
            explanation = "Trust, respect, affection, and happiness are qualitative, continuous needs of the Self ('I'), whereas food and clothing are needs of the Body.",
            difficulty = "EASY"
        ),
        QuizQuestion(
            id = "q_2_3",
            moduleId = "mod_2",
            questionEn = "What is the faculty of 'Imagination' (Kalpana) within the Self composed of?",
            questionTe = "స్వీయంలోని 'కల్పన' వేటితో కూడి ఉంటుంది?",
            optionsJson = "Heartbeats and breathing|Desires (Ichha), Thoughts (Vichar), and Expectations (Asha)|Vision and hearing reflexes|Sleep and hunger cycles",
            correctOptionIndex = 1,
            explanation = "Imagination comprises Desires (aims/goals), Thoughts (reasoning/analysis), and Expectations (selection of sensory inputs).",
            difficulty = "MEDIUM"
        ),
        QuizQuestion(
            id = "q_2_4",
            moduleId = "mod_2",
            questionEn = "What is 'Sanyam' (Self-Regulation) regarding the Body?",
            questionTe = "శరీరానికి సంబంధించి 'సంయం' (స్వీయ క్రమశిక్షణ) అంటే ఏమిటి?",
            optionsJson = "Punishing the body through starvation|The feeling of responsibility in the Self for nurturing, protecting, and rightly utilizing the Body|Total detachment from physical health|Only medical treatments",
            correctOptionIndex = 1,
            explanation = "Sanyam is the feeling of responsibility in 'I' for the proper nurturing, protection, and right utilization of the Body, leading to Swasthya (Health).",
            difficulty = "HARD"
        ),

        // Module 3 Questions
        QuizQuestion(
            id = "q_3_1",
            moduleId = "mod_3",
            questionEn = "What is the foundational value in all human-human relationships?",
            questionTe = "అన్ని మానవ సంబంధాలలో ప్రాథమిక పునాది విలువ ఏది?",
            optionsJson = "Competition|Trust (Vishwas)|Fear of laws|Financial contracts",
            correctOptionIndex = 1,
            explanation = "Trust (Vishwas) is the foundational value, providing the assurance that the other person genuinely intends my well-being.",
            difficulty = "EASY"
        ),
        QuizQuestion(
            id = "q_3_2",
            moduleId = "mod_3",
            questionEn = "Mistrust and interpersonal conflict usually arise when we:",
            questionTe = "అపనమ్మకం మరియు ఘర్షణలు సాధారణంగా ఎప్పుడు ఉత్పన్నమవుతాయి?",
            optionsJson = "Judge ourselves by our intention and judge others by their competence|Help others learn skills|Appreciate differences in opinions|Apologize for mistakes",
            correctOptionIndex = 0,
            explanation = "We give ourselves credit for good intentions, but judge others harshly based on their flawed execution (competence), assuming hostility.",
            difficulty = "MEDIUM"
        ),
        QuizQuestion(
            id = "q_3_3",
            moduleId = "mod_3",
            questionEn = "What does Respect (Samman) mean in UHV?",
            questionTe = "విలువల విద్యలో గౌరవం (సమ్మాన్) అంటే ఏమిటి?",
            optionsJson = "Touching feet of wealthy people|Right Evaluation (Samyak Maan) of the other as they are|Flattering superiors for favors|Giving gifts to seniors",
            correctOptionIndex = 1,
            explanation = "Respect means Right Evaluation (neither over-evaluation, under-evaluation, nor otherwise-evaluation).",
            difficulty = "MEDIUM"
        ),

        // Module 4 & 5 Questions
        QuizQuestion(
            id = "q_4_1",
            moduleId = "mod_4",
            questionEn = "Which of the four orders in Nature is characterized by cellular life, growth, and respiration?",
            questionTe = "ప్రకృతిలోని నాలుగు క్రమాలలో కణ జీవితం, పెరుగుదల మరియు శ్వాసక్రియ లక్షణాలు కలిగినది ఏది?",
            optionsJson = "Material Order|Pranic / Bio Order|Animal Order only|Atomic minerals",
            correctOptionIndex = 1,
            explanation = "The Pranic Order (plants, flora, bio-life) possesses cellular activity, growth, and respiration.",
            difficulty = "EASY"
        ),
        QuizQuestion(
            id = "q_5_1",
            moduleId = "mod_5",
            questionEn = "Which criterion is mandatory for 'Holistic Technology'?",
            questionTe = "'సమగ్ర సాంకేతికత' కోసం తప్పనిసరి ప్రమాణం ఏది?",
            optionsJson = "Maximizing rapid profit at any cost|Eco-friendly, cyclic, and human-friendly design|Creating permanent electronic waste|Replacing all human companionship",
            correctOptionIndex = 1,
            explanation = "Holistic technologies must be cyclic (eco-friendly, zero toxic waste) and human-friendly (promoting health and dignity).",
            difficulty = "MEDIUM"
        )
    )

    fun getSampleCertificates(): List<UhvCertificate> = listOf(
        UhvCertificate(
            certificateId = "UHV-2026-7F3K9P2M",
            studentId = "demo_student_1",
            studentName = "Sample Student",
            rollNo = "25KB1A0000",
            moduleId = "mod_1",
            moduleTitle = "Universal Human Values – Module 1: Self Exploration",
            issueDate = "01 October 2026",
            status = "VALID",
            institutionalAuthority = "NBKRIST UHV Cell Authority",
            verificationHash = "8A4B2C9D1E3F"
        )
    )

    fun getAnnouncements(): List<Announcement> = listOf(
        Announcement(
            id = "ann_1",
            titleEn = "UHV Cell Induction Programme 2026-27 Announced",
            titleTe = "యూహెచ్‌వి సెల్ ఇండక్షన్ ప్రోగ్రామ్ 2026-27 ప్రారంభం",
            content = "All newly enrolled students across all branches are invited to the Universal Human Values Foundation Orientation in the College Auditorium on October 10th, 2026 at 10:00 AM.",
            priority = "HIGH",
            targetAudience = "ALL",
            datePosted = "02 Oct 2026",
            isPinned = true
        ),
        Announcement(
            id = "ann_2",
            titleEn = "Practice Session Reflections Due for Module 1",
            titleTe = "మాడ్యూల్ 1 ప్రాక్టీస్ సెషన్ రిఫ్లెక్షన్స్ సమర్పణ తేదీ",
            content = "Students must submit reflections for PS 1 and PS 2 in the UHV Sadhana portal before attempting the Module 1 Quiz to unlock the verifiable certificate.",
            priority = "NORMAL",
            targetAudience = "ALL",
            datePosted = "01 Oct 2026",
            isPinned = false
        )
    )

    fun getEvents(): List<UhvEvent> = listOf(
        UhvEvent(
            id = "evt_1",
            titleEn = "Harmony in Campus Life: Interactive Workshop",
            titleTe = "క్యాంపస్ జీవితంలో సామరస్యం: ఇంటరాక్టివ్ వర్క్‌షాప్",
            description = "A dynamic interactive session with senior faculty and student mentors exploring interpersonal trust, stress-free learning, and empathetic campus culture.",
            eventDate = "15 Oct 2026",
            eventTime = "02:00 PM - 04:30 PM",
            venue = "Seminar Hall 2, NBKRIST Vidyanagar",
            organizer = "UHV Cell, NBKRIST",
            isRegistered = true
        ),
        UhvEvent(
            id = "evt_2",
            titleEn = "Ecological Stewardship & Campus Tree Plantation Drive",
            titleTe = "పర్యావరణ పరిరక్షణ & క్యాంపస్ మొక్కలు నాటే కార్యక్రమం",
            description = "Practical engagement with Nature's Pranic Order. Join the Green Club and UHV volunteers in planting indigenous saplings across the college botanical zone.",
            eventDate = "22 Oct 2026",
            eventTime = "08:30 AM - 11:30 AM",
            venue = "East Lawn, NBKRIST Campus",
            organizer = "UHV Cell & NSS Unit",
            isRegistered = false
        )
    )

    fun getSampleAuditLogs(): List<AuditLog> = listOf(
        AuditLog(
            actorName = "System",
            actorRole = "SYSTEM",
            action = "DATABASE_INITIALIZATION",
            target = "Curriculum & Seed Data Loaded",
            result = "SUCCESS"
        ),
        AuditLog(
            actorName = "UHV Cell Coordinator",
            actorRole = "ADMIN",
            action = "CERTIFICATE_AUTHORITY_VERIFIED",
            target = "NBKRIST UHV Cell Authority",
            result = "SUCCESS"
        )
    )

    fun getSampleStudentImportData(): String {
        return """Timestamp,Score,FULL NAME,ROLL NO,BRANCH,SECTION,YEAR,MOBILE NUMBER,Email,Profile Image
2026-10-01 10:14:22,85,Rithika Reddy K,23KB1A0589,cse,A,IV,9876501234,rithikareddyk2005@gmail.com,https://images.example.com/p1.jpg
2026-10-01 10:15:30,90,Kalyan Kumar P,23KB1A0590,CSE,B,IV,9876501235,kalyan.p@student.nbkrist.org,
2026-10-01 10:17:11,75,Bhavana M,23KB1A0401,ECE,A,IV,9876501236,bhavana.m@student.nbkrist.org,
2026-10-01 10:18:45,80,Venkata Sai T,23KB1A0402,Electronics,A,IV,9876501237,sai.t@student.nbkrist.org,
2026-10-01 10:20:00,65,Deepak R,24KB1A0101,eee,A,III,9876501238,deepak.r@student.nbkrist.org,
2026-10-01 10:21:12,88,Sravani N,24KB1A0301,Mechanical,A,III,9876501239,sravani.n@student.nbkrist.org,
2026-10-01 10:22:33,92,Arun Prasad G,25KB1A0501,Computer Science and Engineering,A,II,9876501240,arun.p@student.nbkrist.org,
2026-10-01 10:23:44,70,Divya Sree B,25KB1A0502,CSE,A,II,9876501241,divya.b@student.nbkrist.org,
2026-10-01 10:24:55,85,Ramesh K,25KB1A0503,CSE,A,II,9876501242,ramesh.k@student.nbkrist.org,
2026-10-01 10:25:59,82,Sneha L,25KB1A0504,CSE,B,II,9876501243,sneha.l@student.nbkrist.org,
2026-10-01 10:27:01,95,Kiran V,25KB1A0410,ECE,A,II,9876501244,kiran.v@student.nbkrist.org,
2026-10-01 10:28:10,,Invalid Roll Student,,CSE,A,II,9876501245,invalid.roll@example.com,
2026-10-01 10:29:20,80,Rithika Reddy K,23KB1A0589,CSE,A,IV,9876501234,rithikareddyk2005@gmail.com,
2026-10-01 10:30:15,78,Manoj P,25KB1A0201,Civil,A,II,9876501246,manoj.p@student.nbkrist.org,"""
    }

    fun getSampleReflections(): List<DailyUhvReflection> = listOf(
        DailyUhvReflection(
            id = "refl_1",
            userId = "demo_student_1",
            userName = "Sample Student",
            title = "Morning Reflection on Intention vs Competence",
            reflectionText = "Today during my team project meeting, when a teammate did not finish their assigned module, I practiced pausing before reacting. I reminded myself that their intention is to succeed with the team, but they lacked familiarity with the library. Replacing irritation with guidance brought immediate harmony to the group.",
            uhvTheme = "Trust & Relationships",
            sentiment = SentimentCategory.INSIGHTFUL,
            moodRating = 5,
            timestamp = System.currentTimeMillis() - 86400000L * 2,
            dateFormatted = "02 Oct 2026"
        ),
        DailyUhvReflection(
            id = "refl_2",
            userId = "demo_student_1",
            userName = "Sample Student",
            title = "Self-Observation of Sensation Seeking vs Natural Harmony",
            reflectionText = "I observed my urge to continuously scroll social media when tired. I realized this was seeking temporary sensory stimulation rather than nurturing the body with proper rest. When I chose a 20-minute meditation and water instead, I felt deep peace.",
            uhvTheme = "Harmony in the Self",
            sentiment = SentimentCategory.HARMONIOUS,
            moodRating = 4,
            timestamp = System.currentTimeMillis() - 86400000L,
            dateFormatted = "03 Oct 2026"
        ),
        DailyUhvReflection(
            id = "refl_3",
            userId = "demo_student_1",
            userName = "Sample Student",
            title = "Gratitude for Campus Nature and the Pranic Order",
            reflectionText = "Walking past the neem trees outside the NBKRIST library, I felt immense gratitude for the effortless cyclicity of the plant order that breathes oxygen for us without expecting anything in return. I committed to reducing single-use plastic today.",
            uhvTheme = "Harmony in Nature",
            sentiment = SentimentCategory.GRATITUDE,
            moodRating = 5,
            timestamp = System.currentTimeMillis() - 3600000L * 4,
            dateFormatted = "04 Oct 2026"
        )
    )
}
