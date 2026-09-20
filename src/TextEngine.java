import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

/**
 * جزئية ملاك: TextEngine (المحرك الأساسي للنص)
 *
 * المفاهيم الي طبقناها من مادة تراكيب البيانات والخوارزميات:
 * 1. ArrayList -> عشان نخزن الكلمات بالترتيب (Sequential Storage).
 * 2. HashSet -> عشان نحسب الكلمات الفريدة ونقارن المجموعات (Jaccard).
 * 3. HashMap -> عشان نخزن تردد الكلمات (Frequency Map).
 * 4. Stack -> عشان نطبق مبدأ LIFO في خاصية التراجع والإعادة (Undo/Redo).
 * 5. Generics (مثل Stack<String>) عشان نضمن ال Type Safety  .
 * 6. Big-O:موضح فوق كل دالة.
 */



public class TextEngine {

    // المتغيرات الخاصة (Encapsulation)

    private String currentText;                 // النص الحالي (كامل)
    private ArrayList<String> wordsList;        // الكلمات مفردة في ArrayList
    private ArrayList<String> sentencesList;    // الجُمل في ArrayList

    // Stack لتطبيق Undo/Redo (LIFO)
    private Stack<String> undoStack;
    private Stack<String> redoStack;

    // ال (Constructor)
    public TextEngine() {
        this.currentText = "";
        this.wordsList = new ArrayList<>();
        this.sentencesList = new ArrayList<>();
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
    }

    //  دالة تنظيف النص (Pre-processing) - عشان نحقق مبدأ معالجة النصوص
    // ============================================================

    /**
     * تنظيف النص: وهو عبارة عن تحويله ل lowercase، حذف علامات الترقيم، ضغط المسافات.
     *  بنستدعيها عند تحميل النص لأول مرة.
     * التعقيد O(n) حيث n هو طول النص.
     */

    public String cleanText(String rawText) {
        if (rawText == null || rawText.isEmpty()) {
            return "";
        }
        //  هان بنحول النص ل أحرف صغيرة (Lowercase)
        String cleaned = rawText.toLowerCase();

        //  هان بنحذف علامات الترقيم (بنستثني المسافات)
        cleaned = cleaned.replaceAll("[^a-z\\s]", "");

        // هان بنضغط المسافات المتعددة لمسافة وحدة
        cleaned = cleaned.replaceAll("\\s+", " ");

        // هان بنحذف المسافات من البداية والنهاية
        cleaned = cleaned.trim();

        return cleaned;
    }

    // دالة تحميل النص
    // ============================================================

    /**
     * شو بنعمل هان
     * بنحمل النص مباشرة بتستدعيها رشا عند تحميل النص الجديد.
     * بتجمع بين التنظيف والتخزين ومسح الـ Stack في خطوة واحدة.
     * التعقيد O(n) حيث n طول النص.
     */
    public void loadText(String rawText) {
        //  هان بتمسح كل الحالات السابقة لأنه نص جديد
        undoStack.clear();
        redoStack.clear();

        // بتنظف النص
        String cleaned = cleanText(rawText);

        // بتخزن النص
        setText(cleaned);

        System.out.println("تم تحميل النص بنجاح! عدد الكلمات: " + wordsList.size());
    }


    // دوال تخزين واسترجاع النص (ArrayList)
    // ============================================================


    // هان بنخزن النص بعد تنظيفه، وتقسيمه لكلمات وجمل
    // التعقيد O(n) حيث n طول النص

    public void setText(String text) {
        this.currentText = text;

        // هان بقسم النص لكلمات حسب المسافات بخزنها في ArrayList
        if (text != null && !text.isEmpty()) {
            String[] wordsArray = text.split(" ");
            this.wordsList = new ArrayList<>();
            for (String word : wordsArray) {
                if (!word.isEmpty()) {
                    this.wordsList.add(word);
                }
            }

            //هان بقسم النص لجمل حسب . و ! و ?
            String[] sentencesArray = text.split("[.!?]");
            this.sentencesList = new ArrayList<>();
            for (String sentence : sentencesArray) {
                String trimmed = sentence.trim();
                if (!trimmed.isEmpty()) {
                    this.sentencesList.add(trimmed);
                }
            }
        } else {
            this.wordsList = new ArrayList<>();
            this.sentencesList = new ArrayList<>();
        }
    }

     // هان بنرجع النص الحالي كـ String
     // التعقيد O(1)

    public String getCurrentText() {
        return this.currentText;
    }

     // هان برجع قائمة الكلمات ArrayList - عشان تستخدمها رشا
    // بنرجع نسخة جديدة New Copy عشان نضمن التغليف.
    // التعقيد O(n) حيث n عدد الكلمات.
    public ArrayList<String> getWordsList() {
        return this.wordsList;
    }


     //هان برجع نسخة من قائمة الجمل ArrayList عشان استخدمها للبحث الموضعي
    // التعقيد O(n) حيث n عدد الجمل.
    public ArrayList<String> getSentences() {
        return this.sentencesList;
    }

    //  الاستبدال (Atomic Replacement) مع تتبع الحالة
    // ============================================================


    /**
     * شو بنعمل هان
     * استبدال كل ظهور لكلمة بكلمة تانية.
     * قبل الاستبدال بتم حفظ الحالة الحالية في undoStack للتراجع.
     * التعقيد الزمني O(n) حيث n عدد الكلمات لأنها حلقة for.
     * التعقيد المكاني O(1) باستثناء الـ Stack.
     */

    public int replaceWord(String oldWord, String newWord) {
        if (currentText == null || currentText.isEmpty() || oldWord == null || oldWord.isEmpty()) {
            return 0;
        }

        //  هان بحفظ الحالة الحالية في Stack (Undo) - LIFO
        undoStack.push(new String(this.currentText));

        // هان بفرغ Stack الإعادة لأنه عند عملية جديدة، بتفقد صلاحية الـ Redo
        redoStack.clear();

        //  وهان بنفذ الاستبدال
        int count = 0;
        ArrayList<String> newWordsList = new ArrayList<>();
        //O(n)
        for (String word : this.wordsList) {
            if (word.equalsIgnoreCase (oldWord)) {
                newWordsList.add(newWord);
                count++;
            } else {
                newWordsList.add(word);
            }
        }

        //  هان بنحدث النص والقوائم
        this.wordsList = newWordsList;
        this.currentText = String.join(" ", this.wordsList);

        //  هان بنحدث الجمل للبحث الموضعي
        // بعيد بناء الجمل من النص الجديد عشان تكون متطابقة
        String[] newSentences = this.currentText.split("[.!?]");
        this.sentencesList = new ArrayList<>();
        for (String s : newSentences) {
            String trimmed = s.trim();
            if (!trimmed.isEmpty()) {
                this.sentencesList.add(trimmed);
            }
        }

        return count;
    }

    //  التراجع Undo والإعادة Redo عن طريق تطبيق مبدأ Stack (LIFO)
    // ============================================================


    /**
     * شو بعمل هان
     * التراجع عن آخر عملية (Undo).
     * استخدمت ال Stack.pop() عشان استرجع الحالة أو الكلمة السابقة.
     * التعقيد O(1) لكل عملية.
     */
    public void undo() {
        if (undoStack.isEmpty()) {
            System.out.println("لا توجد عمليات سابقة للتراجع عنها.");
            return;
        }

        //هان بحفظ الحالة الحالية في Redo Stack عشان أقدر أعمل Redo
        redoStack.push(new String(this.currentText));

        // هان بسترجع الحالة السابقة من Undo Stack (pop)
        String previousText = undoStack.pop();

        // وهان بطبق الحالة المسترجعة
        setText(previousText);
        System.out.println("تم التراجع بنجاح.");
    }

    /**
     * شو بنعمل هان
     * بنرجع عملية تم التراجع عنها (Redo).
     * باستخدام ال Stack.pop() من Redo Stack.
     * التعقيد O(1) لكل عملية.
     */
    public void redo() {
        if (redoStack.isEmpty()) {
            System.out.println("لا توجد عمليات لإعادتها.");
            return;
        }

        //  بحفظ الحالة الحالية في Undo Stack
        undoStack.push(new String(this.currentText));

        // بسترجع الحالة من Redo Stack (pop)
        String nextText = redoStack.pop();

        // بطبق الحالة المسترجعة
        setText(nextText);
        System.out.println("تمت الإعادة بنجاح.");
    }

    //  قياس التشابه (Jaccard Similarity) - بتطبيق نظرية المجموعات
    // ============================================================

    /**
     * شو بنعمل هان
     * حساب التشابه بين النص الحالي ونص آخر باستخدام Jaccard.
     * بتم تحويل كل نص ل HashSet عشان نتخلص من الكلمات المكررة.
     * القانون J(A,B) = |A ∩ B| / |A ∪ B|
     * التعقيد الزمني O(n + m) حيث n و m هما طول النصين.
     * التعقيد المكاني O(n + m) لتخزين الـ HashSet.
     */
    public double calculateSimilarity(String secondText) {
        if (secondText == null || secondText.isEmpty() || this.currentText == null || this.currentText.isEmpty()) {
            return 0.0;
        }

        // بنظف النص الثاني نفس التنظيف اللي سويناه للأول
        String cleanedSecond = cleanText(secondText);
        String[] secondWordsArray = cleanedSecond.split(" ");

        // بنبني مجموعتين (HashSet) ممنوع تكرار الكلمات
        HashSet<String> set1 = new HashSet<>();
        for (String word : this.wordsList) {
            set1.add(word);
        }

        HashSet<String> set2 = new HashSet<>();
        for (String word : secondWordsArray) {
            if (!word.isEmpty()) {
                set2.add(word);
            }
        }

        // هان بنحسب التقاطع Intersection
        HashSet<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);   // بحتفظ بس بالعناصر المشتركة

        //  بحسب الاتحاد Union
        HashSet<String> union = new HashSet<>(set1);
        union.addAll(set2);

        //  بحسب نسبة التشابه
        if (union.isEmpty()) {
            return 0.0;
        }
        return (double) intersection.size() / union.size();
    }

    //
    //   دالة إحصاء تردد الكلمات باستخدام HashMap
    // ============================================================

    /**
     * هان بنحسب تردد كل كلمة في النص.
     * استخدمنا HashMap<String, Integer> حيث المفتاح = الكلمة، القيمة = التكرار.
     * التعقيد الزمني O(n)
     * التعقيد المكاني O(k) حيث k هو عدد الكلمات الفريدة.
     */
    public HashMap<String, Integer> getWordFrequency() {
        HashMap<String, Integer> freqMap = new HashMap<>();

        for (String word : this.wordsList) {
            // getOrDefault أسلوب مختصر للتعامل مع الـ HashMap
            freqMap.put(word, freqMap.getOrDefault(word, 0) + 1);
        }

        return freqMap;
    }

    //  دالة للحصول على عدد الكلمات الإجمالي
    // ============================================================

    /**
     * بترجع عدد الكلمات الإجمالي.
     * التعقيد O(1)
     */
    public int getTotalWordCount() {
        return this.wordsList.size();
    }

    //  دالة للحصول على الكلمات الفريدة باستخدام HashSet
    // ============================================================

    /**
     * بترجع عدد الكلمات المميزة في النص.
     * بنستخدم HashSet للتخلص من التكرار.
     * التعقيد الزمني O(n)
     * التعقيد المكاني O(k) حيث k عدد الكلمات الفريدة.
     */
    public int getUniqueWordCount() {
        HashSet<String> uniqueWords = new HashSet<>(this.wordsList);
        return uniqueWords.size();
    }

    //  دالة للبحث عن كلمة وإرجاع مواقعها
    // ============================================================

    /**
     * شو بنعمل هان
     * البحث عن كلمة في النص وإرجاع مواقعها.
     * تستخدمها رشا في Positional Search.
     * التعقيد الزمني O(n * m) حيث n عدد الجُمل و m عدد الكلمات في كل جملة.
     */
    public String searchWord(String word) {
        if (word == null || word.isEmpty() || this.sentencesList.isEmpty()) {
            return " الكلمة غير موجودة أو النص فارغ.";
        }

        String searchWord = word.toLowerCase();
        StringBuilder result = new StringBuilder();
        int totalOccurrences = 0;

        for (int i = 0; i < sentencesList.size(); i++) {
            String sentence = sentencesList.get(i);
            String[] sentenceWords = sentence.split(" ");

            for (int j = 0; j < sentenceWords.length; j++) {
                if (sentenceWords[j].equalsIgnoreCase(searchWord)) {
                    result.append(" الجملة رقم ").append(i + 1)
                            .append("، الكلمة رقم ").append(j + 1)
                            .append("\n");
                    totalOccurrences++;
                }
            }
        }

        if (totalOccurrences == 0) {
            return " الكلمة \"" + word + "\" غير موجودة في النص.";
        }

        return " تم العثور على " + totalOccurrences + " ظهور للكلمة \"" + word + "\":\n" + result.toString();
    }

    //  كود رشا
    // ============================================================

    public void displayCharacterStatistics() {
        if (currentText == null || currentText.isEmpty()) {
            System.out.println("النص فارغ، لا توجد إحصائيات للحروف.");
            return;
        }
        int totalCharacters = 0;
        HashMap<Character, Integer> charFrequency = new HashMap<>();
        // بتحول النص ل مصفوفة حروف
        for (char c : currentText.toCharArray()) {
            if (c != ' ') {
                totalCharacters++;
                charFrequency.put(c, charFrequency.getOrDefault(c, 0) + 1);
            }
        }
        System.out.println("إجمالي الحروف (بدون مسافات): " + totalCharacters);
        System.out.println("تكرار الحروف: ");
        for (Character c : charFrequency.keySet()) {
            System.out.print("['" + c + "': " + charFrequency.get(c) + "] ");
        }
        System.out.println();
    }

    public String analyzeSentiment() {
        if (this.wordsList == null || this.wordsList.isEmpty()) return "محايد (لا يوجد نص)";

        HashSet<String> positiveWords = new HashSet<>();
        positiveWords.add("good"); positiveWords.add("great"); positiveWords.add("excellent");
        positiveWords.add("amazing"); positiveWords.add("happy"); positiveWords.add("love");
        positiveWords.add("beautiful"); positiveWords.add("perfect"); positiveWords.add("best");

        HashSet<String> negativeWords = new HashSet<>();
        negativeWords.add("bad"); negativeWords.add("terrible"); negativeWords.add("awful");
        negativeWords.add("sad"); negativeWords.add("hate"); negativeWords.add("worst");
        negativeWords.add("poor"); negativeWords.add("ugly"); negativeWords.add("boring");

        HashSet<String> negations = new HashSet<>();
        negations.add("not"); negations.add("no"); negations.add("never");

        int score = 0;
        boolean negateNext = false;

        for (String word : this.wordsList) {
            if (negations.contains(word)) {
                negateNext = true;
                continue;
            }
            if (positiveWords.contains(word)) {
                score += negateNext ? -1 : 1;
                negateNext = false;
            } else if (negativeWords.contains(word)) {
                score += negateNext ? 1 : -1;
                negateNext = false;
            } else {
                negateNext = false;
            }
        }
        if (score > 0) return "إيجابي (Positive) - النتيجة: " + score;
        else if (score < 0) return "سلبي (Negative) - النتيجة: " + score;
        else return "محايد (Neutral) - النتيجة: " + score;
    }

    //  ميزة ذكية 2: استخراج الكلمات المفتاحية (Mobile Keyword Extraction)
    // ============================================================

    /**
     * استخراج الكلمات المفتاحية الأكثر تكراراً بعد تصفية الكلمات الشائعة (Stop Words).
     * التعقيد الزمني: $O(n \log n)$ بسبب عملية الترتيب (Sorting).
     * التعقيد المكاني: $O(k)$ حيث k عدد الكلمات الفريدة.
     */
    public String extractKeywords(int topN) {
        if (this.wordsList == null || this.wordsList.isEmpty()) {
            return "لا يوجد نص لاستخراج الكلمات المفتاحية.";
        }

        // 1. استخدام HashSet لتصفية الكلمات الشائعة (Stop words) بتعقيد O(1)
        HashSet<String> stopWords = new HashSet<>();
        String[] commonWords = {"the", "is", "in", "at", "of", "on", "and", "a", "to", "it", "for", "with", "as", "by", "this", "that", "are", "was", "be", "or", "an"};
        for (String word : commonWords) {
            stopWords.add(word);
        }

        // 2. حساب تكرار الكلمات المفيدة باستخدام HashMap
        HashMap<String, Integer> keywordFreq = new HashMap<>();
        for (String word : this.wordsList) {
            if (!stopWords.contains(word) && word.length() > 2) { // تجاهل الكلمات القصيرة جداً
                keywordFreq.put(word, keywordFreq.getOrDefault(word, 0) + 1);
            }
        }

        if (keywordFreq.isEmpty()) {
            return "لم يتم العثور على كلمات مفتاحية كافية.";
        }

        // 3. ترتيب الكلمات حسب التكرار (من الأعلى للأقل)
        ArrayList<java.util.Map.Entry<String, Integer>> sortedList = new ArrayList<>(keywordFreq.entrySet());
        sortedList.sort((a, b) -> b.getValue().compareTo(a.getValue())); // ترتيب تنازلي

        // 4. تجهيز النتيجة
        StringBuilder result = new StringBuilder();
        int count = 0;
        for (java.util.Map.Entry<String, Integer> entry : sortedList) {
            if (count >= topN) break;
            result.append("[").append(entry.getKey()).append(": ").append(entry.getValue()).append("] ");
            count++;
        }

        return result.toString();
    }


}










