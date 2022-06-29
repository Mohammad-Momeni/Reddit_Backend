package Database;

import java.util.ArrayList;

public class Post {
    private String postText;
    private String postDate;
    private String postTime;
    private SubReddit subReddit;
    private int likes;
    private int dislikes;
    private ArrayList<Comment> comments;
    Post(String postText, String postDate, String postTime, SubReddit subReddit, int likes, int dislikes) {
        this.postText = postText;
        this.postDate = postDate;
        this.postTime = postTime;
        this.subReddit = subReddit;
        this.likes = likes;
        this.dislikes = dislikes;
        this.comments = new ArrayList<Comment>();
    }
    public String getPostText() {
        return postText;
    }
    public String getPostDate() {
        return postDate;
    }
    public String getPostTime() {
        return postTime;
    }
    public SubReddit getSubReddit() {
        return subReddit;
    }
    public int getLikes() {
        return likes;
    }
    public int getDislikes() {
        return dislikes;
    }
    public ArrayList<Comment> getComments() {
        return comments;
    }
}