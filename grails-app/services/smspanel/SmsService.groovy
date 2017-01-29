package smspanel

interface SmsService {

    boolean send(String message, String phone)
}