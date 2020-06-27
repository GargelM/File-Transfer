/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gargel.file.transfer.server;

import br.com.gargel.file.transfer.bean.ArquivoServer;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Gargel
 */
public class Server {
    public static void main(String[] args) {
        try {
            //1
            ServerSocket srvSocket = new ServerSocket(22222);
            System.out.println("Aguardando envio de arquivo ...");
            Socket socket = srvSocket.accept();
            
            //2
            byte[] objectAsByte = new byte[socket.getReceiveBufferSize()];
            BufferedInputStream bf = new BufferedInputStream(socket.getInputStream());
            bf.read(objectAsByte);
            
            //3
            ArquivoServer arquivoS = (ArquivoServer) getObjectFromByte(objectAsByte);
            
            //4
            String dir = arquivoS.getDiretorioDestino().endsWith("/") ? arquivoS.getDiretorioDestino() + arquivoS.getNome() : arquivoS.getDiretorioDestino() + "/" + arquivoS.getNome();
            System.out.println("Escrevendo arquivo " + dir);
            
            //5
            FileOutputStream fos = new FileOutputStream(dir);
            fos.write(arquivoS.getConteudo());
            fos.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Object getObjectFromByte(byte[] objectAsByte) { 
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        Object obj = null;
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        
        try {
            bis = new ByteArrayInputStream(objectAsByte);
            ois = new ObjectInputStream(bis);
            obj = ois.readObject();
            
            bis.close();
            ois.close();
            
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        
        return obj;
    }
}
