package com.automated.summarizer;

public class FilterNonAscii
{

    String ascii_input_string, FilteredText;

    public String ReplaceNonAscii(String ascii_input)
    {
        this.ascii_input_string = ascii_input;
        // Replace all non ascii chars in the string.
        FilteredText = ascii_input_string.replaceAll("[^\\x0A\\x0D\\x20-\\x7E]", "");;
        return FilteredText;
    }
}
