package ibm.game.server;

import java.util.HashMap;
import io.netty.channel.Channel;
import java.util.*;

public class GameList {
	
	HashMap<String, AGameSession> GameListByID = new HashMap<String, AGameSession>();
	HashMap<Channel, AGameSession> GameListByCH1 = new HashMap<Channel, AGameSession>();
	HashMap<Channel, AGameSession> GameListByCH2 = new HashMap<Channel, AGameSession>();
	
	public synchronized String NewAGame(Channel ch) 
	{
		
		AGameSession game = new AGameSession();
		GameListByID.put(game.getGameid(), game);
		GameListByCH1.put(ch, game);
		game.setC1(ch);
		
		return game.getGameid();
		
		
	}
	
	public synchronized String JoinAGame(String gameid, Channel ch) 
	{
		
		
		AGameSession game = GameListByID.get(gameid);
		
		if (game != null)
		{
		  
		  GameListByCH2.put(ch, game);
		  game.setC2(ch);
		
		  return gameid;
		  
		}
		
		return null;
		
		
	}
	
	
	public synchronized Position getPositionByID(String gameid, Channel ch) 
	{
		
		AGameSession game = GameListByID.get(gameid);
		
		
		return game.getCurPos(ch);
		
		
	}
	
	public synchronized Position getPositionByChannel(Channel ch) 
	{
		
		AGameSession game = GameListByCH1.get(ch);
		if (game==null)
			game = GameListByCH2.get(ch);
		
		if (game != null)
		  return game.getCurPos(ch);
		else 
		  return null;
		
		
	}
	
	public synchronized String getGameIDByChannel(Channel ch) 
	{
		
		AGameSession game = GameListByCH1.get(ch);
	
		if (game==null)
		{
			game = GameListByCH2.get(ch);
			
		}
		
		if (game != null)
		  return game.getGameid();
		else 
		  return null;
		
		
	}
	
	public synchronized boolean isMainPlayer(Channel ch) 
	{
		
		AGameSession game = GameListByCH1.get(ch);
	
		if (game!=null)
		{
			return true;
			
		}
		else
			return false;
		
		
	}
	
	public synchronized void deleteGameByMain(Channel ch) 
	{
		
		AGameSession game = GameListByCH1.get(ch);
		Channel ch2 = game.getC2();
		String gid = game.getGameid();
	
		GameListByCH1.remove(ch);
		GameListByCH2.remove(ch2);
		GameListByID.remove(gid);
		
		game = null;
		
		
	}
	
	public synchronized void setNullSecond(Channel ch)
	{
		AGameSession game = GameListByCH2.get(ch);
		if (game != null)
		{
			GameListByCH2.remove(ch);
			game.setC2(null);
				
		}
		
		
	}
	
	public synchronized ArrayList getAllWaiting()
	{
        ArrayList al = new ArrayList();
        
        Iterator iter = GameListByID.entrySet().iterator(); 
        while (iter.hasNext()) { 
            Map.Entry entry = (Map.Entry) iter.next(); 
            Object key = entry.getKey(); 
            AGameSession game = (AGameSession)entry.getValue(); 
            
            if (game.getC2() == null)
            {
            	al.add(key);
            	
            	
            }
            
        } 
        
        
        
        
        return al;
        
		
		
	}
	
	public synchronized Position move(String gameid, Channel ch, int xstep, int ystep)
	{
		AGameSession game = GameListByID.get(gameid);
		Position p = null;
		if (game != null)
		{
			p = game.Move(ch, xstep, ystep);
				
		}
		
		return p;
	}
	
	

}