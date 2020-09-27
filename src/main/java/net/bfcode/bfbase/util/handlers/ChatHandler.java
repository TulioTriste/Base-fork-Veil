package net.bfcode.bfbase.util.handlers;

public class ChatHandler
{
    private static int chatDelay;
    private static boolean chatToggled;
    
    public static boolean isMuted() {
        return ChatHandler.chatToggled;
    }
    
    public static void setChatToggled(final boolean chat) {
        ChatHandler.chatToggled = chat;
    }
    
    public static int getChatDelay() {
        return ChatHandler.chatDelay;
    }
    
    public static void setChatDelay(final int time) {
        ChatHandler.chatDelay = time;
    }
    
    static {
        ChatHandler.chatDelay = 0;
        ChatHandler.chatToggled = false;
    }
}
