import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class Bot extends TelegramLongPollingBot {
    private static final List<String> idsOfChats = Arrays.asList("-1001666965863", "-1001814404189");
    private static final String MESSAGE_FOR_START = "Привет!Подпишись на следующие каналы и получи свой бонус!\nКаналы:\n" +
            "https://t.me/+ivoJJaAeQFBlOTQy\n" +
            "https://t.me/test_channell_9248";
    private final String MESSAGE_FOR_HELP = "Чтобы получить бонус тебе нужно подписаться на каналы, которые были указаны выше";
    private final String MESSAGE_FOR_ACCEPTED_VERIFICATION = "Отлично!Твой бонус равняется " + new Random().nextInt(100, 200) + " гривен";
    private final String MESSAGE_FOR_REJECTED_VERIFICATION = "Ты не подписался на все каналы. Проверь еще раз";

    public static void main(String[] args) {
        try {
            Bot bot = new Bot();
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.getStackTrace();
        }

    }

    @Override
    public String getBotUsername() {
        return "test9248_bot";
    }

    @Override
    public String getBotToken() {
        return "6846767074:AAGWnwhRHZETgP5J5T9IQL3o8Wt9FDgjitg";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            String username = message.getFrom().getUserName();
            String messageText = message.getText();
            String chatId = message.getChatId().toString();
            switch (messageText) {
                case "/start":
                    sendMessage(chatId, MESSAGE_FOR_START);
                    break;
                case "/help":
                    sendMessage(chatId, MESSAGE_FOR_HELP);
                    break;
                case "/check":
                    if (isUserSubscribed(update.getMessage().getFrom().getId())) {
                        sendMessage(chatId, MESSAGE_FOR_ACCEPTED_VERIFICATION);
                    } else {
                        sendMessage(chatId, MESSAGE_FOR_REJECTED_VERIFICATION);
                    }
                    break;
                default:
                    sendMessage(chatId, "Default" + username);
                    break;
            }
        }
    }

    public void sendMessage(String chatId, String text) {
        SendMessage message = new SendMessage();
        message.setText(text);
        message.setChatId(chatId);
        try {
            executeAsync(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public boolean isUserSubscribed(Long userId) {
        try {
            for (var id : idsOfChats) {
                GetChatMember member = new GetChatMember();
                member.setChatId(id);
                member.setUserId(userId);
                ChatMember theChatMember = execute(member);
                if (theChatMember.getStatus().equals("left")) {
                    return false;
                }
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return true;
    }
}
