package M;

public class Track {
    private String title;
    private String filePath;

    public Track(String title, String filePath) {
        this.title = title;
        this.filePath = filePath;
    }

    // Explicitly define getTitle()
    public String getTitle() { 
        return this.title; 
    }

    // Explicitly define getFilePath()
    public String getFilePath() { 
        return this.filePath; 
    }
}

