import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {
    private final Map<String, List<PageEntry>> data = new TreeMap<>();

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        for (File pdf : Objects.requireNonNull(pdfsDir.listFiles(), "Неверный путь к файлам")) {
            var doc = new PdfDocument(new PdfReader(pdf));
            for (int i = 0; i < doc.getNumberOfPages(); i++) {
                int currentPage = i + 1;
                var text = PdfTextExtractor.getTextFromPage(doc.getPage(currentPage));
                var words = text.split("\\P{IsAlphabetic}+");
                Map<String, Integer> freqs = new TreeMap<>();
                for (var word : words) {
                    if (word.isEmpty()) {
                        continue;
                    }
                    word = word.toLowerCase();
                    freqs.put(word, freqs.getOrDefault(word, 0) + 1);
                }

                for (var entry : freqs.entrySet()) {
                    List<PageEntry> wordSearchingResult;
                    if (data.containsKey(entry.getKey())) {
                        wordSearchingResult = data.get(entry.getKey());
                    } else {
                        wordSearchingResult = new ArrayList<>();
                    }
                    wordSearchingResult.add(new PageEntry(pdf.getName(), currentPage, entry.getValue()));
                    Collections.sort(wordSearchingResult, Collections.reverseOrder());
                    data.put(entry.getKey(), wordSearchingResult);
                }
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        return data.get(word.toLowerCase());
    }
}
