package net.sbarbaro.filemaster.model;

/**
 * FileCriterion
 * <p>
 * {Purpose of This Class}
 * <p>
 * {Other Notes Relating to This Class (Optional)}
 *
 * @author sab $LastChangedRevision: $
 * $LastChangedDate: $
 */
public enum FileCriterion {

    NAME("File name"), 
    EXT("File extension"), 
    SIZE("File size"),
    TYPE("File type"),
    CONTENTS("File contents"), 
    CREATED("File created"), 
    MODIFED("File modified"), 
    ACCESSED("File accessed"),
    IMAGE_ASPECT_RATIO("Image aspect ratio");

    FileCriterion(String text) {
        this.text = text;
    }
    private final String text;

    @Override
    public String toString() {
        return text;
    }
}
