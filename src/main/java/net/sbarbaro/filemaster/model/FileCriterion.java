package net.sbarbaro.filemaster.model;

/**
 * FileCriterion
 * <p>
 * An enumeration of the attributes of a file that can be used as a filter
 * <p>
 * @author Anthony J. Barbaro (tony@abarbaro.net) $LastChangedRevision: $
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

    @Override
    public String toString() {
        return text;
    }

    private FileCriterion(String text) {
        this.text = text;
    }
    private final String text;
}
