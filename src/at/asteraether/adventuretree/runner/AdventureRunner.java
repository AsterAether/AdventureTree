package at.asteraether.adventuretree.runner;

import at.asteraether.adventuretree.adventure.Adventure;
import at.asteraether.adventuretree.adventure.TextSpeed;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class AdventureRunner {
    public static void main(String[] args) {

        File target = null;
        TextSpeed speed = null;
        if (args.length >= 1) {
            for (String arg :
                    args) {
                if (arg.startsWith("--file=")) {
                    target = new File(System.getProperty("user.dir") + File.separator + arg.replace("--file=", ""));
                } else if (arg.startsWith("--speed=")) {
                    switch (arg.replace("--speed=", "")) {
                        case "s":
                        case "slow":
                            speed = TextSpeed.SLOW;
                            break;
                        case "n":
                        case "normal":
                            speed = TextSpeed.NORMAL;
                            break;
                        case "f":
                        case "fast":
                            speed = TextSpeed.FAST;
                            break;
                        case "i":
                        case "instant":
                            speed = TextSpeed.INSTANT;
                            break;
                    }
                }
            }
        }

        if (target == null) {
            JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
            chooser.setFileFilter(new FileNameExtensionFilter("Adventure Files", "adv"));
            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                target = chooser.getSelectedFile();
            }
        }

        if (target == null) {
            return;
        }


        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(target));
            Adventure adv = (Adventure) ois.readObject();
            if (speed != null) {
                adv.getIoHandler().setTextSpeed(speed);
            }
            ois.close();

            adv.play();
            adv.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

    }
}
