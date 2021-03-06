package Database;

import java.util.ArrayList;

public class SubReddit {
    private String subRedditName;
    private String subRedditDescription;
    private String subRedditType;
    private ArrayList<Post> posts;
    private int postCount;
    private User moderator;
    SubReddit(String subRedditName, String subRedditDescription, User moderator, String subRedditType) {
        this.subRedditName = subRedditName;
        this.subRedditType = subRedditType;
        this.posts = new ArrayList<Post>();
        this.postCount = 0;
        this.moderator = moderator;
        this.subRedditDescription = subRedditDescription;
    }
    public String getSubRedditName() {
        return subRedditName;
    }
    public String getSubRedditDescription() {
        return subRedditDescription;
    }
    public ArrayList<Post> getPosts() {
        return posts;
    }
    public int getPostCount() {
        return postCount;
    }
    public User getModerator() {
        return moderator;
    }
    public void setModerator(User moderator) {
        this.moderator = moderator;
    }
    public void addPost(Post post) {
        posts.add(post);
        postCount++;
    }
    public void removePost(Post post) {
        post.getPoster().removePost(post);
        posts.remove(post);
        postCount--;
    }
    public String getSubRedditType() {
        return subRedditType;
    }
    public String toString() {
        return subRedditName + "," + subRedditDescription + "," + subRedditType + "," + postCount + "," + moderator + "\n";
    }
}
