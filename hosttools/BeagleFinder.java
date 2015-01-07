import java.io.*;
import java.util.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;

public class BeagleFinder implements AddressAdder
{
    public static void main(String[] args)
    {
        Listener beagleListener;
        GUI gui;

        if (GraphicsEnvironment.isHeadless())
        {
            beagleListener = new Listener(new BeagleFinder());
            System.out.println("Listening for Beagles...");
            EventQueue.invokeLater(beagleListener);
        }
        else
        {
            gui = new GUI();
            beagleListener = new Listener(gui);
            EventQueue.invokeLater(gui);
            (new Thread(beagleListener)).start();
        }
    }

    public void addAddress(String address)
    {
        System.out.println(address);
    }
}

interface AddressAdder
{
    public void addAddress(String address);
}

class Listener implements Runnable
{
    private static String MULTICAST_ADDRESS = "255.255.255.255";
    private static int PORT = 64649; // "MINIX" on telephone keypad
    private static int MAXBUF = 2048;
    private static AddressAdder _lister;

    public Listener(AddressAdder adder)
    {
        _lister = adder;
    }

    public void run()
    {
        // Set of all Beagles already found
        HashSet<InetAddress> seenAddrs = new HashSet<>();
        MulticastSocket s = null;
        try
        {
            s = new MulticastSocket(PORT);
        }
        catch (IOException e)
        {
            System.err.println("Unable to create socket");
            System.exit(1);
        }

        // while forever (or until ^C)
        while (true)
        {
            byte[] buf = new byte[MAXBUF];
            DatagramPacket recv = new DatagramPacket(buf, buf.length);

            // Get a packet
            try
            {
                s.receive(recv);
            }
            catch (IOException e)
            {
                System.err.println("Unable to receive packet");
                System.exit(1);
            }

            // Print the details of the packet as long as we haven't seen one
            // from that IP address before
            if (!seenAddrs.contains(recv.getAddress()))
            {
                // Find the first NULL character, which indicates the end of
                // the string.  This fixes a bug on Windows, which prints all of
                // the null characters as whitespace. *NIX machines ignore the 
                // characters like they're supposed to.
                int i = 0;
                for (i = 0; i < MAXBUF && buf[i] != '\0'; i++) { }

                String ident = new String(buf, 0, i);
                // "<DATA>" found at /IP.ADD.RE.SS
                _lister.addAddress('"' + ident + 
                    "\" found at " + recv.getAddress().toString());

                // Add the IP to the list of already-found-Beagles
                seenAddrs.add(recv.getAddress());
            }
        }
    }
}

class GUI extends JFrame implements AddressAdder, Runnable
{
    private DefaultListModel _lstModel;
    private JList _lstBeagles;

    private void initUI()
    {
        Container pane = getContentPane();

        _lstModel = new DefaultListModel();
        _lstBeagles = new JList(_lstModel);
        JScrollPane listScroller = new JScrollPane(_lstBeagles);

        pane.add(listScroller);

        pack();
        setTitle("Beagle Finder");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void run()
    {
        addAddress("Listening for Beagles...");
        setVisible(true);
    }

    public void addAddress(String item)
    {
        _lstModel.addElement(item);
    }

    public GUI()
    {
        initUI();
    }
}
