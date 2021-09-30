package top.wuare.http.helper.multipart;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MultiPartFormData {

    private final List<MultiPartTextItem> textItems = new ArrayList<>();
    private final List<MultiPartFileItem> fileItems = new ArrayList<>();

    public MultiPartTextItem getTextItem(String name) {
        Optional<MultiPartTextItem> first = textItems.stream().filter(e -> name.equals(e.getName())).findFirst();
        return first.orElse(null);
    }

    public List<MultiPartTextItem> getTextItems() {
        return textItems;
    }

    public MultiPartFileItem getFileItem(String name) {
        Optional<MultiPartFileItem> first = fileItems.stream().filter(e -> name.equals(e.getName())).findFirst();
        return first.orElse(null);
    }

    public List<MultiPartFileItem> getFileItems() {
        return fileItems;
    }

    public void addFileItem(MultiPartFileItem item) {
        fileItems.add(item);
    }

    public void addTextItem(MultiPartTextItem item) {
        textItems.add(item);
    }

    public void clear() {
        fileItems.forEach(MultiPartFileItem::clear);
    }
}
