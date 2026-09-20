import java.util.HashMap;
import java.util.Scanner;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    //نقطة البداية بتنشئ الكائنات وبتشغل loadInitialText() وبتدير القائمة الرئيسية
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TextEngine engine = new TextEngine();

        System.out.println("==================================================");
        System.out.println("  مرحباً بك في LexiCore: Mobile Text Processing Engine  ");
        System.out.println("==================================================");

        //بتعرض خيارين لتحميل النص (كتابة مباشرة أو من ملف) O(K) حيث K عدد السطور
        loadInitialText(scanner, engine);

        boolean isRunning = true;

        //حلقة مستمرة بتعرض القائمة عشان يختار المستخدم
        while (isRunning) {
            System.out.println("\n--- القائمة الرئيسية (Background Services) ---");
            System.out.println("1. عرض لوحة الإحصائيات (Analytics Dashboard)");
            System.out.println("2. البحث الموضعي عن كلمة (Positional Search)");
            System.out.println("3. استبدال كلمة (Atomic Word Replacement)");
            System.out.println("4. التراجع عن آخر عملية (Undo)");
            System.out.println("5. إعادة آخر عملية (Redo)");
            System.out.println("6. فحص نسبة التشابه مع نص آخر (Similarity Detector)");
            System.out.println("7. محلل المشاعر الفوري (Sentiment Analyzer)");
            System.out.println("8. استخراج الكلمات المفتاحية (Keyword Extraction)");
            System.out.println("9. تحميل نص جديد");
            System.out.println("0. إغلاق البرنامج (Shutdown)");
            System.out.print("الرجاء اختيار رقم العملية: ");

            String choice = scanner.nextLine();

            //O(1) بختار خيار
            switch (choice) {
                case "1":
                    System.out.println("\n-- إحصائيات النص --");
                    System.out.println("إجمالي الكلمات: " + engine.getTotalWordCount());
                    System.out.println("الكلمات الفريدة: " + engine.getUniqueWordCount());

                    System.out.println("\n-- تردد الكلمات --");
                    HashMap<String, Integer> freq = engine.getWordFrequency();
                    if (freq.isEmpty()) {
                        System.out.println("النص فارغ.");
                    } else {
                        for (String key : freq.keySet()) {
                            System.out.println(key + ": " + freq.get(key));
                        }
                    }

                    engine.displayCharacterStatistics();
                    break;
                case "2":
                    System.out.print("أدخل الكلمة للبحث عنها: ");
                    String search = scanner.nextLine();
                    System.out.println(engine.searchWord(search));
                    break;
                case "3":
                    System.out.print("أدخل الكلمة المراد استبدالها: ");
                    String oldWord = scanner.nextLine();
                    System.out.print("أدخل الكلمة الجديدة: ");
                    String newWord = scanner.nextLine();
                    long startTime = System.currentTimeMillis();
                    int count = engine.replaceWord(oldWord, newWord);
                    long endTime = System.currentTimeMillis();
                    long duration = endTime - startTime;
                    System.out.println("تم استبدال " + count + " كلمة بنجاح.");
                    System.out.println("الوقت المستغرق للعملية: " + duration + " ميلي ثانية (ms).");
                    System.out.println("\n النص بعد الاستبدال:");
                    System.out.println(engine.getCurrentText());
                    break;
                case "4":
                    engine.undo();
                    System.out.println("\n النص بعد التراجع:");
                    System.out.println(engine.getCurrentText());
                    break;
                case "5":
                    engine.redo();
                    System.out.println("\n النص بعد الإعادة:");
                    System.out.println(engine.getCurrentText());
                    break;
                case "6":
                    System.out.print("أدخل النص الآخر للمقارنة: ");
                    String otherText = scanner.nextLine();
                    double similarity = engine.calculateSimilarity(otherText);
                    System.out.println("نسبة التشابه (Jaccard): " + (similarity * 100) + "%");
                    break;
                case "7":
                    System.out.println("\n-- تحليل المشاعر --");
                    System.out.println("النتيجة: " + engine.analyzeSentiment());
                    break;
                case "8":
                    System.out.println("\n-- استخراج الكلمات المفتاحية --");
                    System.out.print("كم كلمة مفتاحية تريد استخراجها؟ (مثال: 3 أو 5): ");
                    try {
                        int topN = Integer.parseInt(scanner.nextLine());
                        System.out.println("أبرز الكلمات: " + engine.extractKeywords(topN));
                    } catch (NumberFormatException e) {
                        System.out.println("الرجاء إدخال رقم صحيح.");
                    }
                    break;
                case "9":
                    loadInitialText(scanner, engine);
                    break;
                case "0":
                    isRunning = false;
                    System.out.println("جاري إغلاق محرك LexiCore. وداعاً!");
                    break;
                default:
                    System.out.println("خيار غير صحيح، الرجاء إدخال رقم من القائمة.");
            }
        }
        scanner.close();
    }

    private static void loadInitialText(Scanner scanner, TextEngine engine) {
        System.out.println("\n--- خيارات تحميل النص ---");
        System.out.println("1. إدخال النص مباشرة (Direct Keyboard Stream)");
        System.out.println("2. قراءة من ملف محلي (Local Storage File Path)");
        System.out.print("اختر طريقة الإدخال (1 أو 2): ");
        String inputMethod = scanner.nextLine();

        if (inputMethod.equals("1")) {
            System.out.println("أدخل النص الآن (اكتب $$END_TEXT$$ في سطر جديد للإنهاء):");
            StringBuilder sb = new StringBuilder();
            while (true) {
                String line = scanner.nextLine();
                if (line.trim().equals("$$END_TEXT$$")) {
                    break;
                }
                //O(1) لأنه بوسع نفسه تلقائيا
                sb.append(line).append(" ");
            }
            engine.loadText(sb.toString());

            //O(n) حيث n  حجم الملف
        } else if (inputMethod.equals("2")) {
            System.out.print("أدخل المسار المطلق للملف (مثال: C:\\files\\chat_log.txt): ");
            String filePath = scanner.nextLine();
            if (filePath.startsWith("\"") && filePath.endsWith("\"")) {
                filePath = filePath.substring(1, filePath.length() - 1);
            }
            try {
                String content = new String(Files.readAllBytes(Paths.get(filePath)));
                engine.loadText(content);
            } catch (IOException e) {
                System.out.println("خطأ: لم يتم العثور على الملف أو تعذرت قراءته. تأكد من المسار وجرب مرة أخرى.");
            }
        } else {
            System.out.println("خيار غير صحيح. سيتم البدء بنص فارغ.");
        }
    }
}