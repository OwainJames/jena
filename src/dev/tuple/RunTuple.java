/*
 * (c) Copyright 2007 Hewlett-Packard Development Company, LP
 * All rights reserved.
 * [See end of file]
 */

package dev.tuple;

import arq.cmd.CmdUtils;

import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.sdb.layout2.TableDescTriples;
import com.hp.hpl.jena.sdb.layout2.hash.StoreBaseHash;
import com.hp.hpl.jena.sdb.layout2.hash.TupleLoaderOneHash;
import com.hp.hpl.jena.sdb.layout2.index.StoreBaseIndex;
import com.hp.hpl.jena.sdb.layout2.index.TupleLoaderOneIndex;
import com.hp.hpl.jena.sdb.store.Store;
import com.hp.hpl.jena.sdb.store.StoreFactory;
import com.hp.hpl.jena.sdb.store.StoreLoader;
import com.hp.hpl.jena.sdb.store.TupleGraphLoader;
import com.hp.hpl.jena.sparql.sse.SSE;


public class RunTuple
{  
    static { CmdUtils.setLog4j() ; }

    public static void main(String...argv)
    {
        boolean reset = true ;

        Store store = StoreFactory.create("sdb.ttl") ;
        if ( reset )
            store.getTableFormatter().create() ;

        //store.getConnection().setLogSQLStatements(true) ;

        Triple t = SSE.parseTriple("(triple <http://host/foo> 2 3)") ;
        StoreLoader sLoader = store.getLoader() ;
        
        if ( store instanceof StoreBaseHash )
            sLoader = new TupleGraphLoader(new TupleLoaderOneHash(store, new TableDescTriples())) ;
        if ( store instanceof StoreBaseIndex )
            sLoader = new TupleGraphLoader(new TupleLoaderOneIndex(store, new TableDescTriples())) ;

        sLoader.startBulkUpdate() ;
        sLoader.addTriple(t) ;
        sLoader.addTriple(SSE.parseTriple("(triple <http://host/foo> 2 99)")) ;

        sLoader.deleteTriple(t) ;
        sLoader.deleteTriple(SSE.parseTriple("(triple <http://host/foo> 2 5)")) ;
        sLoader.finishBulkUpdate() ;
        System.out.println("** Finished") ;
        System.exit(0) ;

    }
}

/*
 * (c) Copyright 2007 Hewlett-Packard Development Company, LP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */