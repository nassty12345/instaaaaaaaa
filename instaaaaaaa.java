import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.InstagramException;
import org.brunocvcunha.instagram4j.requests.InstagramGetMediaCommentsRequest;
import org.brunocvcunha.instagram4j.requests.InstagramGetMediaInfoRequest;
import org.brunocvcunha.instagram4j.requests.InstagramGetRecentHashtagsRequest;
import org.brunocvcunha.instagram4j.requests.InstagramGetUserRequest;
import org.brunocvcunha.instagram4j.requests.payload.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InstagramHashtagParser {
    private static final String USERNAME = "your_username";
    private static final String PASSWORD = "your_password";

    public static void main(String[] args) {
        try {
            Instagram4j instagram = Instagram4j.builder().username(USERNAME).password(PASSWORD).build();
            instagram.setup();
            instagram.login();

            // Список пользователей, для которых нужно собрать хештеги
            List<String> userList = new ArrayList<>();
            userList.add("username1");
            userList.add("username2");
            userList.add("username3");

            for (String user : userList) {
                // Получение информации о пользователе
                InstagramGetUserResult userResult = instagram.sendRequest(new InstagramGetUserRequest(user));
                long userId = userResult.getUser().getPk();

                // Получение последней публикации пользователя
                InstagramGetRecentHashtagsResult recentMediaResult = instagram.sendRequest(new InstagramGetRecentHashtagsRequest(userId));
                if (recentMediaResult.getItems().size() > 0) {
                    Item recentMediaItem = recentMediaResult.getItems().get(0);
                    String mediaId = recentMediaItem.getPk();

                    // Получение информации о публикации
                    InstagramGetMediaInfoResult mediaInfoResult = instagram.sendRequest(new InstagramGetMediaInfoRequest(mediaId));
                    InstagramMedia media = mediaInfoResult.getItems().get(0);

                    // Получение комментариев к публикации
                    InstagramGetMediaCommentsResult commentsResult = instagram.sendRequest(new InstagramGetMediaCommentsRequest(mediaId));
                    List<InstagramComment> comments = commentsResult.getComments();

                    // Вывод хештегов публикации
                    for (String hashtag : media.getCaption().getHashtags()) {
                        System.out.println("Хештег: " + hashtag);
                    }

                    // Вывод хештегов комментариев
                    for (InstagramComment comment : comments) {
                        for (String hashtag : comment.getHashtags()) {
                            System.out.println("Хештег комментария: " + hashtag);
                        }
                    }
                }
            }

            instagram.logout();
        } catch (IOException | InstagramException e) {
            e.printStackTrace();
        }
    }
}