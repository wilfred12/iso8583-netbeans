/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISORequestListener;
import org.jpos.iso.ISOSource;

/**
 *
 * @author phenom
 */
public class Listener implements ISORequestListener{

   
    public boolean process(ISOSource isos, ISOMsg isomsg) {
        
        try {
            isomsg.setResponseMTI();
            isomsg.set(39,"00");
            isomsg.unpack(isomsg.getBytes());
            isomsg.dump(System.out, "");
            isos.send(isomsg);
        } catch (ISOException ex) {
            Logger.getLogger(Listener.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ioe){
            
        }
        throw new UnsupportedOperationException("Not supported yet.");
        
    }
    
}
