// 
package org.bad.inf;

import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISORequestListener;
import org.jpos.iso.ISOSource;
import org.jpos.space.Space;
import org.jpos.space.SpaceFactory;
import org.jpos.transaction.Context;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;



public class InfoListener implements ISORequestListener {

  private ApplicationContext appContext;

    public InfoListener() {
       appContext = new FileSystemXmlApplicationContext("/conf/applicationContext.xml");
    }

    public boolean process(ISOSource isos, ISOMsg isomsg) {
        Context ctx = new Context();
        Space sp = SpaceFactory.getSpace();
        ctx.put("SRC", isos);
        ctx.put("REQ", isomsg);
      ctx.put("SPRING", appContext);
        sp.out("TXNMGR", ctx);
        return true;
    }

}
