package gui;

import model.WavFile;
import operations.Autocorrelation;
import operations.Cepstrum;
import operations.Operations;
import operations.Transformable;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import static operations.Transformable.newline;

public class Main extends JFrame implements ActionListener {
    // paths section
    private JLabel originalSoundPathTextLabel;
    private JTextField originalSoundPathTextInput;
    private JButton originalSoundFileChooserButton;

    private JTextField chunkSizeTextField;
    private JLabel chunkSizeLabel;

    private JButton startAutocorrelation;
    private JButton startCepstrum;

    private Operations operations;
    private WavFile sourceFile;
    private JTextPane resultsPane;

    private File selectedFile;

    public static final String defaultImagesPath = "src/main/resources/sounds";

    public Main() {
        super("Sound processing");
        getContentPane().setLayout(null);

        initializeLogicComponents();
        initializeGraphicComponents();
        initializeListeners();
        initWindow();
    }

    private void initWindow() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
        setSize(800, 500);
    }

    private void initializeListeners() {
    }

    private void initializeGraphicComponents() {
        originalSoundPathTextLabel = new JLabel("Original sound path");
        originalSoundPathTextLabel.setBounds(10, 15, 120, 17);
        getContentPane().add(originalSoundPathTextLabel);

        originalSoundPathTextInput = new JTextField();
        originalSoundPathTextInput.setBounds(130, 10, 491, 27);
        originalSoundPathTextInput.setEnabled(false);
        originalSoundPathTextInput.setColumns(10);
        getContentPane().add(originalSoundPathTextInput);

        originalSoundFileChooserButton = new JButton("Choose sound");
        originalSoundFileChooserButton.setBounds(10, 50, 145, 29);
        getContentPane().add(originalSoundFileChooserButton);
        originalSoundFileChooserButton.addActionListener(this);


        chunkSizeLabel = new JLabel("Chunk size");
        chunkSizeLabel.setBounds(10, 96, 90, 30);
        getContentPane().add(chunkSizeLabel);

        chunkSizeTextField = new JTextField();
        chunkSizeTextField.setColumns(10);
        chunkSizeTextField.setBounds(80, 100, 92, 27);
        chunkSizeTextField.setText("44100");
        getContentPane().add(chunkSizeTextField);


        startAutocorrelation = new JButton("Autocorrelation");
        startAutocorrelation.setBounds(10, 140, 130, 60);
        getContentPane().add(startAutocorrelation);
        startAutocorrelation.addActionListener(this);

        startCepstrum = new JButton("Cepstrum");
        startCepstrum.setBounds(150, 140, 130, 60);
        getContentPane().add(startCepstrum);
        startCepstrum.addActionListener(this);

        resultsPane = new JTextPane();
        resultsPane.setBounds(300, 45, 300, 300);
        getContentPane().add(resultsPane);

    }

    private void initializeLogicComponents() {
        operations = new Operations();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == originalSoundFileChooserButton) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(defaultImagesPath));
            int returnValue = fileChooser.showOpenDialog(Main.this);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
                originalSoundPathTextInput.setText(selectedFile.getPath());
                reload();
            }
        } else if (e.getSource() == startAutocorrelation) {
            resultsPane.setText("Starting autocorrelation" + newline);
            operations.clear();
            Transformable autocorrelation = new Autocorrelation();
            autocorrelation.setChunkSize(Integer.parseInt(chunkSizeTextField.getText()));
            operations.addOperation(autocorrelation);
            StringBuilder builder = operations.processSound(sourceFile);
            resultsPane.setText(resultsPane.getText() + builder.toString());
        } else if (e.getSource() == startCepstrum) {
            resultsPane.setText("Starting cepstrum analysis" + newline);
            operations.clear();
            Transformable cepstrum = new Cepstrum();
            cepstrum.setChunkSize(Integer.parseInt(chunkSizeTextField.getText()));
            operations.addOperation(cepstrum);
            StringBuilder builder = operations.processSound(sourceFile);
            resultsPane.setText(resultsPane.getText() + builder.toString());
        }
    }

    private void reload() {
        try {
            sourceFile = WavFile.openWavFile(selectedFile);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(selectedFile);
            AudioFormat format = audioInputStream.getFormat();
            long frames = audioInputStream.getFrameLength();
            double durationInSeconds = (frames + 0.0) / format.getFrameRate();
            sourceFile.setDuration(durationInSeconds);
            sourceFile.setName(selectedFile.getName());
        } catch (Exception ex) {
            System.out.println("===ERROR===");
            System.out.println("An error occurred when trying to read .wav file");
            ex.printStackTrace();
            System.out.println("===ERROR===");
        }
    }

}
