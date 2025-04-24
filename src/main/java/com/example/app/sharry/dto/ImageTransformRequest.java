package com.example.app.sharry.dto;

public class ImageTransformRequest {
    private Transformations transformations;

    public Transformations getTransformations() {
        return transformations;
    }

    public void setTransformations(Transformations transformations) {
        this.transformations = transformations;
    }

    public static class Transformations {
        private Resize resize;
        private Crop crop;
        private Integer rotate;
        private String format;
        private Filters filters;

        public Resize getResize() {
            return resize;
        }

        public void setResize(Resize resize) {
            this.resize = resize;
        }

        public Crop getCrop() {
            return crop;
        }

        public void setCrop(Crop crop) {
            this.crop = crop;
        }

        public Integer getRotate() {
            return rotate;
        }

        public void setRotate(Integer rotate) {
            this.rotate = rotate;
        }

        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
        }

        public Filters getFilters() {
            return filters;
        }

        public void setFilters(Filters filters) {
            this.filters = filters;
        }
    }

    public static class Resize {
        private int width;
        private int height;
        // Getters and Setters

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }
    }

    public static class Crop {
        private int width;
        private int height;
        private int x;
        private int y;

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }
    }

    public static class Filters {
        private boolean grayscale;
        private boolean sepia;

        public boolean isGrayscale() {
            return grayscale;
        }

        public void setGrayscale(boolean grayscale) {
            this.grayscale = grayscale;
        }

        public boolean isSepia() {
            return sepia;
        }

        public void setSepia(boolean sepia) {
            this.sepia = sepia;
        }
    }
}
