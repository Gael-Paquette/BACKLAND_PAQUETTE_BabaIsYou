package fr.esiee.babaisyou.view;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ImagesEndLoader {
  private final HashMap<String, Image> images = new HashMap<>();

  private String createPath(String name) {
    Objects.requireNonNull(name);
    return "/images/END/" + name + ".png";
  }

  private void loadImage(String name, String path) {
    Objects.requireNonNull(name);
    Objects.requireNonNull(path);

    var imageUrl = ImagesEndLoader.class.getResource(path);
    if (imageUrl == null) {
      throw new IllegalStateException("Image not found: " + path);
    }
    images.put(name, new ImageIcon(imageUrl).getImage());
  }

  private void initImages(List<String> imagesEnd) {
    Objects.requireNonNull(imagesEnd);
    imagesEnd.forEach(image -> {
      var path = createPath(image);
      loadImage(image, path);
    });
  }

  public ImagesEndLoader(List<String> imagesEnd) {
    initImages(imagesEnd);
  }

  public Image getImage(String name) { return images.getOrDefault(name, null); }

  public Map<String, Image> getImages() { return images; }
}
