package eu.telecomnancy.pcd2k17;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import javafx.scene.control.*;
import javafx.stage.Stage;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Group;
import org.gitlab4j.api.models.Member;
import org.gitlab4j.api.models.Project;

public class GmailSTMP implements Initializable {

    private Group thisgroup;
    private GitLabApi gitlab;

    private boolean error = false;

    @FXML
    Label groupe;

    @FXML
    TextField id_fill;

    @FXML
    PasswordField password_fill;

    @FXML
    TextField subject;

    @FXML
    TextArea message;

    @FXML
    Button sendButton;

    public GmailSTMP(Group group, GitLabApi gl){

        thisgroup=group;
        gitlab=gl;
    }

    public void sendMailTool(String id, String mdp, String destinataire, String sujet, String corps) {
        final String username = id;
        final String password = mdp;

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(destinataire));
            message.setSubject(sujet);
            message.setText(corps);

            Transport.send(message);
        } catch (MessagingException e) {
            this.error=true;
            //throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        groupe.setText(thisgroup.getName());
    }


    public void sendMail(){
        ArrayList<MemberInformations> list = GroupConfiguration.getById(thisgroup.getId()).getMembersList();
        for (MemberInformations m : list){
            sendMailTool(id_fill.getText(),password_fill.getText(),m.getEmail(),subject.getText(),message.getText());
        }
        if(!error){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Delivery message");
            alert.setHeaderText(null);
            alert.setContentText("Le mail a été envoyé à tous les destinataires.");
            alert.showAndWait();
            Stage stage = (Stage) sendButton.getScene().getWindow();
            stage.close();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Echec d'envoi");
            alert.setHeaderText(null);
            alert.setContentText("Le mail n'as pas pu être achimené correctement.\n\nVérifiez vos identifiants de connexion.");
            alert.showAndWait();
            this.error=false;
        }

    }
}