package Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import Database.Database;

public class Controller {
    private String login(String username, String password) {
        try {
            ArrayList<String> data = Database.getInstance().getTable("&&User").get();
            for (String line : data) {
                if(line.equals(username)) {
                    ArrayList<String> userData = Database.getInstance().getTable(username).get();
                    String[] userDataArray = userData.get(0).split(",");
                    if(userDataArray[1].equals(password)) {
                        return "success";
                    }
                }
            }
            return "wrong username or password";
        } catch (IOException e) {
            return "error";
        }
    }
    private String signUp(String username, String password, String email) {
        Pattern pattern1 = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher1 = pattern1.matcher(email);
        Pattern pattern2 = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
        Matcher matcher2 = pattern2.matcher(password);
        if(!matcher1.find()) {
            return "email invalid";
        } else if(!matcher2.find()) {
            return "password invalid";
        } else {
            try {
                if(isDuplicate(username, email)) {
                    return "duplicate";
                }
                Database.getInstance().getTable("&&User").insert(username,true);
                Database.getInstance().addTable(username, "./Data/" + username + ".txt");
                Database.getInstance().getTable(username).insert(username + "," + password + "," + email,true);
                return "success";
            } catch(Exception e) {
                return "error";
            }
        }
    }
    private String feed() {
        try {
            ArrayList<String> input = Database.getInstance().getTable("&&Post").get();
            input = Sortfeed(input);
            StringBuilder output = new StringBuilder("success\n");
            for(String line : input) {
                output.append(line + "\n");
            }
            output.deleteCharAt(output.length() - 1);
            return output.toString();
        } catch (IOException e) {
            return "error";
        }
    }
    private String dLike(String postText,boolean Like) {
        try {
            ArrayList<String> input = Database.getInstance().getTable("&&Post").get();
            for(int i=0;i<input.size();i++) {
                String[] postData = input.get(i).split("&&");
                if(postData[0].equals(postText)) {
                    if(Like) {
                        postData[4] = Integer.toString(Integer.parseInt(postData[4]) + 1);
                    } else {
                        postData[4] = Integer.toString(Integer.parseInt(postData[4]) - 1);
                    }
                }
                if(i == 0) {
                    Database.getInstance().getTable("&&Post").insert(postData[0] + "&&" + postData[1] + postData[2] + "&&" + postData[3] + postData[4] + "&&" + postData[5] + "&&" + postData[6], false);
                } else {
                    Database.getInstance().getTable("&&Post").insert(postData[0] + "&&" + postData[1] + postData[2] + "&&" + postData[3] + postData[4] + "&&" + postData[5] + "&&" + postData[6], true);
                }
            }
            return "success";
        } catch(Exception e) {
            return "error";
        }
    }
    private String addPost(String author, String postText, String subRedditName, String date) {
        try {
            Database.getInstance().getTable("&&Post").insert(subRedditName + "&&" + author + "&&" + date + "&&" + postText + "&&" + "0" + "&&" + "0" + "&&" + "0", true);
            return "success";
        } catch(Exception e) {
            return "error";
        }
    }
    private String addSubReddit(String subRedditName, String subRedditType) {
        try {
            ArrayList<String> input = Database.getInstance().getTable("&&subReddit").get();
            for(String line : input) {
                String name = line.split("&&")[0];
                if(name.equals(subRedditName)) {
                    return "duplicate";
                }
            }
            if(subRedditType.equals("Type")) {
                subRedditType = "public";
            }
            Database.getInstance().getTable("&&subReddit").insert(subRedditName + "&&" + subRedditType, true);
            return "success";
        } catch(Exception e) {
            return "error";
        }
    }
    private String getSubReddit() {
        try {
            ArrayList<String> input = Database.getInstance().getTable("&&subReddit").get();
            StringBuilder output = new StringBuilder("success\n");
            for(String line : input) {
                output.append(line + "\n");
            }
            output.deleteCharAt(output.length() - 1);
            return output.toString();
        } catch(Exception e) {
            return "error";
        }
    }
    private String editProfile(String username,String newUsername, String newPassword, String newEmail) {
        Pattern pattern1 = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher1 = pattern1.matcher(newEmail);
        Pattern pattern2 = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
        Matcher matcher2 = pattern2.matcher(newPassword);
        if(!matcher1.find()) {
            return "email invalid";
        } else if(!matcher2.find()) {
            return "password invalid";
        } else if (isDuplicate(newUsername, newEmail)) {
            return "duplicate";
        }
        try {
            ArrayList<String> input = Database.getInstance().getTable("&&User").get();
            for(int i=0;i<input.size();i++) {
                if(input.get(i).equals(username)) {
                    input.set(i, newUsername);
                }
                if(i == 0) {
                    Database.getInstance().getTable("&&User").insert(input.get(i), false);
                } else {
                    Database.getInstance().getTable("&&User").insert(input.get(i), true);
                }
            }
            Database.getInstance().changeTable(username, newUsername, "./Data/" + newUsername + ".txt");
            Database.getInstance().getTable(newUsername).insert(newUsername + "," + newPassword + "," + newEmail,true);

            return "success";
        } catch(Exception e) {
            return "error";
        }
    }
    public String run(String request) {
        String[] requestParts = request.split("&&");
        switch (requestParts[0]) {
            case "login":
                return login(requestParts[1], requestParts[2]);
            case "signUp":
                return signUp(requestParts[1], requestParts[2], requestParts[3]);
            case "feed":
                return feed();
            case "dLike":
                return dLike(requestParts[1], Boolean.parseBoolean(requestParts[2]));
            case "addPost":
                return addPost(requestParts[1], requestParts[2], requestParts[3], requestParts[4]);
            case "addSubReddit":
                return addSubReddit(requestParts[1], requestParts[2]);
            case "getSubReddit":
                return getSubReddit();
            case "editProfile":
                return editProfile(requestParts[1], requestParts[2], requestParts[3], requestParts[4]);
            default:
                return "invalid request";
        }
    }
    private boolean isDuplicate(String username,String email) {
        try {
            ArrayList<String> data = Database.getInstance().getTable("&&User").get();
            for (String line : data) {
                if(line.equals(username)) {
                    return true;
                }
                ArrayList<String> userData = Database.getInstance().getTable(line).get();
                String[] userDataArray = userData.get(0).split(",");
                if(userDataArray[2].equals(email)) {
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            return false;
        }
    }
    private ArrayList<String> Sortfeed(ArrayList<String> input) {
        ArrayList<String> output = input;
        //sort by date
        for(int i=0;i<output.size();i++) {
            String[] postData = input.get(i).split("&&");
            for(int j=i+1;j<output.size();j++) {
                String[] postData2 = input.get(j).split("&&");
                String[] postDate = postData[2].split("-");
                String[] postDate2 = postData2[2].split("-");
                if(Integer.parseInt(postDate[0]) < Integer.parseInt(postDate2[0])) {
                    String temp = output.get(i);
                    output.set(i, output.get(j));
                    output.set(j, temp);
                } else if(Integer.parseInt(postDate[0]) == Integer.parseInt(postDate2[0])) {
                    if(Integer.parseInt(postDate[1]) < Integer.parseInt(postDate2[1])) {
                        String temp = output.get(i);
                        output.set(i, output.get(j));
                        output.set(j, temp);
                    } else if(Integer.parseInt(postDate[1]) == Integer.parseInt(postDate2[1])) {
                        if(Integer.parseInt(postDate[2]) < Integer.parseInt(postDate2[2])) {
                            String temp = output.get(i);
                            output.set(i, output.get(j));
                            output.set(j, temp);
                        }
                    }
                }
            }
        }
        return output;
    }
}