package com.example.annonymouschat;

public class commentClass {

    String commenterName,commenterImage,commenterId,comment,time,likes,dislike,commentId;
    double credit;
    boolean upvoted,downvoted;

    public commentClass(String commenterName, String commenterImage, String commenterId, String comment, String time, String likes, String dislike, double credit,String commentId) {
        this.commenterName = commenterName;
        this.commenterImage = commenterImage;
        this.commenterId = commenterId;
        this.comment = comment;
        this.time = time;
        this.likes = likes;
        this.dislike = dislike;
        this.credit = credit;
        this.commentId = commentId;
        this.upvoted = false;
        this.downvoted = false;
    }

    public String getCommenterName() {
        return commenterName;
    }

    public void setCommenterName(String commenterName) {
        this.commenterName = commenterName;
    }

    public String getCommenterImage() {
        return commenterImage;
    }

    public void setCommenterImage(String commenterImage) {
        this.commenterImage = commenterImage;
    }

    public String getCommenterId() {
        return commenterId;
    }

    public void setCommenterId(String commenterId) {
        this.commenterId = commenterId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getDislike() {
        return dislike;
    }

    public void setDislike(String dislike) {
        this.dislike = dislike;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public boolean isUpvoted() {
        return upvoted;
    }

    public void setUpvoted(boolean upvoted) {
        this.upvoted = upvoted;
    }

    public boolean isDownvoted() {
        return downvoted;
    }

    public void setDownvoted(boolean downvoted) {
        this.downvoted = downvoted;
    }

    public String getCommentId() {
        return commentId;
    }
}
