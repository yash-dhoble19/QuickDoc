package com.example.myapplication34;

import java.util.List;

public class GeminiRequest {
    List<Content> contents;

    public GeminiRequest(List<Content> contents) {
        this.contents = contents;
    }

    public static class Content {
        List<Part> parts;

        public Content(List<Part> parts) {
            this.parts = parts;
        }
    }

    public static class Part {
        String text;

        public Part(String text) {
            this.text = text;
        }
    }
}
