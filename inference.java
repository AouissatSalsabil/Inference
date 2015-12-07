package ai;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
class Predicate
{
    String fname;
    HashMap<String,String> var=new HashMap<String,String>();
    List<String> var1=new ArrayList<String>();
    Boolean tfvalue=true;
    Boolean visited=false;
    Predicate(Predicate p)
    {
        this.fname=p.fname;
        this.tfvalue=p.tfvalue;
        this.var.putAll(p.var);
        this.var1.addAll(p.var1);
    }
    Predicate(String q)
    { 
        if(q.charAt(0)=="~".charAt(0))
        {
            tfvalue=false;
            fname=q.substring(0,q.indexOf("("));
        }
        else
        {
            fname=q.substring(0,q.indexOf("("));
        }
        //System.out.println("printing q - "+ q);
        String temp=q.substring(q.indexOf("(")+1,q.indexOf(")"));
        temp.trim();
        String tempi[]=temp.split(",");
        for(int i=0;i<tempi.length;i++)
        {
            
            tempi[i].trim();
            //System.out.println("confirm - "+tempi[i]);
            if(tempi[i].length()==1)
            {
                var.put(tempi[i],tempi[i]);
                var1.add(tempi[i]);
            }
            else
            {
                var.put(tempi[i],"true");
                var1.add(tempi[i]);
            }
        }
    }
    //@Override
    public boolean equalspredicate(Predicate p)
    {
        if(this.fname.equals(p.fname))
        {
            for(int i=0;i<this.var1.size();i++)
            {
                if(this.var1.get(i).equals(p.var1.get(i)));
                else
                return false;
            }
            return true;
        }
        return false;
    }
    void print()
    {
       System.out.println("Name - "+fname);
       for(Map.Entry<String, String> entry : var.entrySet())
       {
           //System.out.println(entry.getKey());
       }
       for(int i=0;i<var1.size();i++)
       {
           System.out.println(var1.get(i));
       }
       
    }
}
class Statement
{
    List<Predicate> LHS=new ArrayList<Predicate>();
    Predicate RHS;
    Predicate fact;
    boolean factOrFunction=false;
    Statement(String stmt)
    {
        //System.out.println("Statement is "+stmt);
        if(stmt.contains("=>"))
        {
            String left=stmt.substring(0,stmt.indexOf("=>"));
            String right=stmt.substring(stmt.indexOf("=>")+3);
            RHS=new Predicate(right);
            //System.out.println("left is - "+left);
            String lefty[]=left.split("\\^ ");
            
            //System.out.println("after conversion - ");
            for(int po=0;po<lefty.length;po++)
            {
                System.out.println(lefty[po]);
            }
            for(int i=0;i<lefty.length;i++)
            {
                if(lefty[i].length()>2)
                {
                    lefty[i].trim();
                    //System.out.print("Popoar");
                    //System.out.println(lefty[i]);
                    Predicate a=new Predicate(lefty[i]);
                    LHS.add(a);
                }
            }
        }
        else
        {
            factOrFunction=true;
            fact=new Predicate(stmt);
        }
    }
    void print()
    {
        if(RHS!=null)
        {
            System.out.print("RHS = ");
            RHS.print();
            
        }
        if(LHS.size()!=0)
        {
            System.out.print("LHS = ");
            for(int i=0;i<LHS.size();i++)
            {
                LHS.get(i).print();
            }
        }
        if(fact!=null)
        {
            System.out.print("Fact = ");
            fact.print();
        }
    }
}
class knowledgebase
{
   HashMap<String,List<Statement>> var=new HashMap<String,List<Statement>>();
   HashMap<String,List<Statement>>  add(List<Statement> state)
   {
       for(int i=0;i<state.size();i++)
       {
           if(!state.get(i).factOrFunction)
           {
               if(!var.containsKey(state.get(i).RHS.fname))
                {
                    List<Statement> temp=new ArrayList<Statement>();
                    temp.add(state.get(i));
                    var.put(state.get(i).RHS.fname,temp);
                }
                else
                {
                    List<Statement> temp=var.get(state.get(i).RHS.fname);
                    temp.add(state.get(i)); 
                    if(state.get(i).LHS.get(0).fname.equals("Mother"))
                    {
                        for(int k=0;k<temp.size();k++)
                        {
                            //temp.get(k).print();
                        }
                        List<Statement> tempo=new ArrayList<Statement>(var.get(state.get(i).RHS.fname));
                        for(int k=0;k<tempo.size();k++)
                        {
                            //tempo.get(k).print();
                        }
                        
                    }
                }
           }
           else
           {
               if(!var.containsKey(state.get(i).fact.fname))
                {
                    List<Statement> temp=new ArrayList<Statement>();
                    temp.add(state.get(i));
                    var.put(state.get(i).fact.fname,temp);
                }
                else
                {
                   var.get(state.get(i).fact.fname).add(state.get(i));
                }
           }
       }
       return var;
   }
   void print()
   {
        for(Map.Entry<String,List<Statement>> entry : var.entrySet())
        {
           System.out.print("the key is "+entry.getKey()+"  -  ");
           for(int j=0;j<entry.getValue().size();j++)
           {
               entry.getValue().get(j).print();
           }
        }
  
   }
}
class inference 
{
        static List<Predicate> visited=new ArrayList<Predicate>();
        //static Boolean visited[];
        static List<Predicate> func = new ArrayList<>();
        static List<Predicate> query=new ArrayList<>();
        static List<Statement> state = new ArrayList<>();
        static HashMap<String,List<Statement>> knowledge=new HashMap<String,List<Statement>>();
        static List<HashSet> set=new ArrayList<HashSet>();
        static knowledgebase kb;
        static int counter=0;
        
    
    public static void main(String[] args) throws FileNotFoundException, IOException 
    {
        //consider the case where a fact is on LHS and a variable on RHS
        File f = new File("output.txt");
        FileOutputStream fos = new FileOutputStream(f);
        PrintWriter pw = new PrintWriter(fos);
        File inFile=new File(args[1]);
        //List<function> func = new ArrayList<>();
        //List<Statement> state = new ArrayList<>();
        BufferedReader br = null;
        List<String> input = new ArrayList<String>();
        try 
        {
            br = new BufferedReader(new FileReader(inFile));
            	while(true)
            	{
            		String s=br.readLine();
            		if(s!=null)
            		input.add(s);
            		else 
            		break;
            	}
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        } 
        finally 
        {
            try 
            {
            if (br != null)br.close();
            } 
            catch (IOException ex) 
            {
            ex.printStackTrace();
            }
        }
        int numberofqueries=Integer.parseInt(input.get(0));
        //visited=new Boolean[numberofqueries];
        for(int ct=0;ct<numberofqueries;ct++)
        {
            query.add(new Predicate(input.get(ct+1)));
        }
        int numberoffacts=Integer.parseInt(input.get(numberofqueries+1));
        for(int i=0;i<numberoffacts;i++)
        {
            //Pattern p;
            //p = Pattern.compile("(~)?[A-Z]\\(([a-z]|[A-Z][A-Za-z]{1,})((,([a-z]|[A-Z][A-Za-z]{1,}))+)?\\)( \\^ (~)?[A-Z]\\(([a-z]|[A-Z][A-Za-z]{1,})((,([a-z]|[A-Z][A-Za-z]{1,}))+)?\\))?( => (~)?[A-Z]\\(([a-z]|[A-Z][A-Za-z]{1,})((,([a-z]|[A-Z][A-Za-z]{1,}))+)?\\))?");
            //Matcher m=p.matcher(input.get(numberofqueries+2+i));
            //while(m.find())
                //{
                    //System.out.println(m.group().trim());
                    int c=0;
                    String temp=input.get(numberofqueries+2+i);
                    String kameha[]=temp.split("=>");
                    String literal[]=kameha[0].split("^");
                    //System.out.println(Arrays.toString(literal));
                    for(int k=0;k<literal.length;k++)
                    {
                        if(literal[k].length()>2)
                        {
                            //System.out.println("just chill");
                            Predicate a=new Predicate(literal[k]);
                            //a.print();
                            func.add(a);
                        }
                    }
                    if(kameha.length>1)
                    {
                        kameha[1].trim();
                        Predicate a=new Predicate(kameha[1]);
                        func.add(a);
                    }
                    Statement b=new Statement(temp);
                    //b.print();
                    state.add(b);
                    
                //}
            
        }
        //standardize();
        kb=new knowledgebase();
        for(int i=0;i<state.size();i++)
        {
            //state.get(i).print();
        }
        knowledge=kb.add(state);
        //kb.print();
        for(int ct=0;ct<numberofqueries;ct++)
        {
            HashMap<String,String> substitution=new HashMap<String,String>();
            if(backward_chaining(query.get(ct)))
            {
                pw.write("TRUE");
                pw.write(System.getProperty("line.separator"));
                
            }
            else
            {
                pw.write("FALSE");
                pw.write(System.getProperty("line.separator")); 
            }
            System.out.println("STOP");
            set.clear();
            for(int h=0;h<query.size();h++)
            {
                query.get(h).visited=false;
            }
        }
        pw.flush();
        fos.close();
        pw.close();
        //Handling variable loops
        
        //boolean check=checkquery(input.get(1));
        //System.out.println(check);
    }
    static Boolean backward_chaining(Predicate p)
    {
        List<Predicate> visited_new=new ArrayList<Predicate>();
        List<HashMap<String,String>> substitutionx=new ArrayList<HashMap<String,String>>();
        //System.out.println("OR on ");
        //p.print();
        HashMap<String,String> substitution = new HashMap<String,String>();
        substitutionx=backward_OR(p, substitution,visited_new);
        //for(int i=0;i<substitutionx.size();i++)
        //{
          //  System.out.println(substitutionx.get(i).toString());
        //}
        if(substitutionx==null)
        return false;
        else
        return true;
    }
    static List<HashMap<String,String>> backward_OR(Predicate p,HashMap<String,String> substitution,List<Predicate> visited_new )
    {
        //System.out.print("visited");
        //for(int i=0;i<visited_new.size();i++)
        //{
          //  visited_new.get(i).print();
        //}
        //System.out.println(set.toString());
           //System.out.print("the current Substitution list is as follows - ");
           //System.out.println(substitution.toString());
        for(int ip=0;ip<query.size();ip++)
        {
           if(query.get(ip).equalspredicate(p))
           {
               if(query.get(ip).visited==false)
                   query.get(ip).visited=true;
               else
               return null;
           }
       }
       List<Predicate> visited_or=new ArrayList<Predicate>();
       visited_or.addAll(visited_new);
       if(checkfact(p))
       {
           for(int ip=0;ip<visited_or.size();ip++)
           {
               if(visited_or.get(ip).equalspredicate(p))
               {
                   int x=0;
                   return null;
               }
           }
           visited_or.add(p);
       }
       List<HashMap<String,String>> ultimate=new ArrayList<HashMap<String,String>>();
       HashMap<String,String> substitution1=new HashMap<String,String>(substitution);
       HashMap<String,String> substitution2=new HashMap<String,String>();
       List<Statement> statement_list = new ArrayList<Statement>();
       List<Boolean> boolean_checker=new ArrayList<Boolean>();
       HashMap<String,String> unifylist=new HashMap<String,String>();
       HashMap<String,String> and_reply=new HashMap<String,String>();
       if(knowledge.containsKey(p.fname))
       statement_list.addAll(knowledge.get(p.fname));
       else 
       return null;
       statement_list=arrangeorder(statement_list);
       int gp=0;
       if(p.fname.equals("F"))
       {
         //System.out.print("kamehameha");
           //for(int i=0;i<statement_list.size();i++)
           //statement_list.get(i).print();
       }
       for(int i=0;i<statement_list.size();i++)
       {
           if(!statement_list.get(i).factOrFunction)
           {
               if(p.var1.size()==statement_list.get(i).RHS.var1.size())
               {
                    modifyorder(statement_list.get(i));
                    //System.out.print("Unification on - ");
                    for(int ip=0;ip<p.var1.size();ip++)
                    {
                        //System.out.println(p.var1.get(ip));
                    }
                    for(int ip=0;ip<p.var1.size();ip++)
                    {
                         //System.out.println(statement_list.get(i).RHS.var1.get(ip));
                    }
                    //statement_list.get(i).RHS.var1.toString();
                    //System.out.println("before standardization");
                    //statement_list.get(i).print();
                    statement_list.set(i,standardize(statement_list.get(i)));
                    //kb.print();
                    //System.out.println("after standardization");
                    //statement_list.get(i).print();
                    System.out.println("List 1");
                    System.out.println(p.var1.toString());
                    System.out.println("List 2");
                    System.out.println(statement_list.get(i).RHS.var1.toString());
                    unifylist=unify(p.var1,statement_list.get(i).RHS.var1,substitution1);
               
                    if(unifylist!=null)
                    {
                        //boolean_checker.add(Boolean.TRUE);
                        if(p.fname.equals("Father"))
                        {
                            System.out.print("Father");
                            System.out.println(unifylist.toString());
                        }
                        //if(p.fname.equals("A"))
                        //{
                            //System.out.println("checking for A");
                        //}
                        System.out.println(unifylist.toString());
                        //System.out.println(unifylist.toString());
                        System.out.print("And ON ");
                        statement_list.get(i).LHS.get(0).print();
                        substitution1=unifylist;
                        HashMap<String,String> temp=new HashMap<String,String>();
                        temp=backward_AND(statement_list.get(i).LHS,substitution1,visited_or);
                        if(temp!=null)
                        {
                            boolean_checker.add(Boolean.TRUE);
                            substitution2=new HashMap<String,String>(temp);
                            ultimate.add(temp);
                        }
                        boolean_checker.add(Boolean.FALSE);
                        //if(substitution1==null)
                        //return null;
                    }
                    else
                    boolean_checker.add(Boolean.FALSE);
                    int popat=1;
                }
           }
           else
           {
               if(p.var1.size()==statement_list.get(i).fact.var1.size())
               {
                   //System.out.print("Unification on - ");
                    for(int ip=0;ip<p.var1.size();ip++)
                    {
                        //System.out.println(p.var1.get(ip));
                    }
                    for(int ip=0;ip<p.var1.size();ip++)
                    {
                         //System.out.println(statement_list.get(i).fact.var1.get(ip));
                    }
                    System.out.println("List 1");
                    System.out.println(p.var1.toString());
                    System.out.println("List 2");
                    System.out.println(statement_list.get(i).fact.var1.toString());
                   unifylist=unify(p.var1,statement_list.get(i).fact.var1,substitution1);
                   //if(p.fname.equals("Father"))
                   //System.out.println(unifylist.toString());
                   if(unifylist!=null)
                   {
                       boolean_checker.add(Boolean.TRUE);
                       //substitution1=unifylist; Old Line
                       //substitution2=new HashMap<String,String>(unifylist);
                       ultimate.add(unifylist);
                       System.out.println(substitution1.toString());
                   }
                   else
                   {
                       boolean_checker.add(Boolean.FALSE);
                       System.out.println("null returned");
                   }
                    int popat=1;
                    //return null;
                   //System.out.println("checking for A");
               }
           }
       }
       boolean temp=true;
       for(int i=0;i<boolean_checker.size();i++)
       {
           if(!boolean_checker.get(i));
           else
           temp=false;
       }
       if(temp)
       return null;
       else
       {
           //System.out.print("ultimate   ");
           for(int y=0;y<ultimate.size();y++)
           {
               //System.out.println(ultimate.get(y).toString());
           }
           return ultimate;
       }
       
       
    }
    static HashMap<String,String> backward_AND(List<Predicate> lhs,HashMap<String,String> substitution,List<Predicate> visited_and)
    {
    	//check failure conditions later
        List<Predicate> lefthandside=new ArrayList<Predicate>(lhs);
        HashMap<String,String> substitution_and=new HashMap<String,String>(substitution);
    	List<HashMap<String,String>> substitution_ultimate=new ArrayList<HashMap<String,String>>();
    	Predicate temp=substitute(lefthandside.get(0),substitution);
        //System.out.print("Or ON");
        temp.print();
        if(temp==null)
        System.out.println("predicate error");
        else if(substitution==null)
        System.out.println("sub list error");
        else;
        substitution_ultimate=backward_OR(temp,substitution,visited_and);
        
        if(substitution_ultimate==null)
            return null;
        lefthandside.remove(lefthandside.get(0));
        for(int i=0;i<substitution_ultimate.size();i++)
        {
            
            if(!lefthandside.isEmpty())
            {
                substitution_and=backward_AND(lefthandside,substitution_ultimate.get(i),visited_and);
                if(substitution_and==null);       
                else
                return substitution_and;
            }
            else
            return substitution_ultimate.get(i);
        }
    	return substitution_and;
    }
    static List<Statement> arrangeorder(List<Statement> statement_list)
    {
        List<Statement> temporary1=new ArrayList<Statement>();
        List<Statement> temporary2=new ArrayList<Statement>();
        for(int i=0;i<statement_list.size();i++)
        {
            if(statement_list.get(i).factOrFunction)
            temporary1.add(statement_list.remove(i));
        }
        temporary2.addAll(statement_list);
        statement_list.clear();
        statement_list.addAll(temporary1);
        statement_list.addAll(temporary2);
        return statement_list;
    }
    static Predicate substitute(Predicate p1,HashMap<String,String> substitution)
    {
        Predicate p=new Predicate(p1);
        for(int i=0;i<p.var1.size();i++)
        {
            if(substitution.containsKey(p.var1.get(i)))
            {
                //System.out.println("check - "+i);
                p.var1.set(i,substitution.get(p.var1.get(i)));
            }
            else;
        }
        //System.out.print("After Substitution - ");
        //p.print();
        return p;
    }
    static HashMap<String,String> unify(List<String> goal, List<String> rhs,HashMap<String,String> substitution1)
    {
       
        List<String> goal_variable=new ArrayList<String>(goal);
        List<String> rhs_variable=new ArrayList<String>(rhs);
        HashMap<String,String> unify_list=new HashMap<String,String>(substitution1);
        for(int i=0;i<goal_variable.size();i++)
        {
            if(Character.isUpperCase(goal_variable.get(i).charAt(0)))
            {
                if(Character.isUpperCase(rhs_variable.get(i).charAt(0)))
                {
                    if(goal_variable.get(i).equals(rhs_variable.get(i)));
                    else
                    return null;
                }
                else
                {
                    if(unify_list.containsKey(rhs_variable.get(i)))
                    {
                        String temp=unify_list.get(rhs_variable.get(i));
                        if(temp.equals(goal_variable.get(i)));
                        else
                        return null;
                    }
                    else
                    {
                        if(!checkset(rhs_variable.get(i)))
                        {
                            unify_list.put(rhs_variable.get(i),goal_variable.get(i));
                            //HashSet<String> a=new HashSet<String>();
                            //a.add(rhs_variable.get(i));
                            //set.add(a);
                        }
                        else
                        {
                            HashSet<String> temp=new HashSet<String>();
                            temp=getvariables(rhs_variable.get(i));
                            Iterator iterator = temp.iterator();
                            while(iterator.hasNext())
                            {
                                String p=""+iterator.next();
                                unify_list.put(p,goal_variable.get(i));
                            }
                            
                        }
                    }
                }
            }    
            else
            {
                if(Character.isUpperCase(rhs_variable.get(i).charAt(0)))
                {
                    if(unify_list.containsKey(goal_variable.get(i)))
                    {
                        if(unify_list.get(goal_variable.get(i)).equals(rhs_variable.get(i)));
                        else 
                            return null;
                            
                    }
                    unify_list.put(goal_variable.get(i),rhs_variable.get(i));
                    HashSet<String> temp=new HashSet<String>();
                    temp=getvariables(goal_variable.get(i));
                    Iterator iterator = temp.iterator();
                    while(iterator.hasNext())
                    {
                        String p=""+iterator.next();
                        unify_list.put(p,rhs_variable.get(i));
                    }
                    
                }
                else
                {
                    if(unify_list.containsKey(rhs_variable.get(i)))
                    unify_list.put(goal_variable.get(i),unify_list.get(rhs_variable.get(i)));
                    else
                    {
                        if(unify_list.containsKey(goal_variable.get(i)))
                        unify_list.put(rhs_variable.get(i),unify_list.get(goal_variable.get(i)));
                    }
                    if(checkset(rhs_variable.get(i)))
                    {
                        if(!checkset(goal_variable.get(i)))
                        addset(goal_variable.get(i),rhs_variable.get(i));
                        else
                        merge(goal_variable.get(i),rhs_variable.get(i));
                    }
                    if(checkset(goal_variable.get(i)))
                    {
                       addset(rhs_variable.get(i),goal_variable.get(i)); 
                    }
                    else
                    {
                        HashSet<String> temp=new HashSet<String>();
                        temp.add(goal_variable.get(i));
                        temp.add(rhs_variable.get(i));
                        set.add(temp);
                    }
                    
                }
                
            }
        }
        /*
        if(!Character.isUpperCase(goal_variable.get(i).charAt(0)) && Character.isUpperCase(rhs_variable.get(i).charAt(0)))
                    {
                        if(!unify_list.containsKey(goal_variable.get(i)))
                        unify_list.put(goal_variable.get(i),rhs_variable.get(i));
                    }
        */
        /*for(int i=0;i<rhs.size();i++)
        {
            if(Character.isUpperCase(goal.get(i).charAt(0)) && Character.isUpperCase(rhs.get(i).charAt(0)))
            {
                
                if(goal.get(i).equals(rhs.get(i)));
                else
                return null;
            }
            else 
            {
                if(Character.isUpperCase(goal.get(i).charAt(0)) && !Character.isUpperCase(rhs.get(i).charAt(0)));
                else
                {
                    if(!unify_list.containsKey(goal.get(i)))
                    {
                        //System.out.println("just chill - "+goal.get(i));
                        unify_list.put(goal.get(i), rhs.get(i));
                    }
                    else
                    {
                
                        String temp=unify_list.get(goal.get(i));
                        if(temp.equals(rhs.get(i)));
                        else
                        {
                            if(Character.isUpperCase(goal.get(i).charAt(0)))
                            {
                                return null;
                            }
                    
                        }
                    }      
                }
            }
        }
        //System.out.println("Unification");
        //System.out.println(unify_list.toString());*/
        return unify_list;
    }
    static Boolean checkfact(Predicate p)
    {
        if(query.contains(p))
        return false;
        for(int i=0;i<p.var1.size();i++)
        {
            if(Character.isUpperCase(p.var1.get(i).charAt(0)));
            else
            return false;
        }
        return true;
    }
    static Boolean checkquery(Predicate p)
    {
        if(query.contains(p))
        {
            if(p.visited==false)
            {
                p.visited=true;
                return true;
            }
            return false;
            
        }
        return false;
    }
    static void merge(String bow,String arrow)
    {
        int a=100,b=200;
        for(int i=0;i<set.size();i++)
        {
            if(set.get(i).contains(arrow))
            a=i;
        }
        for(int i=0;i<set.size();i++)
        {
            if(set.get(i).contains(bow))
            b=i;
        }
        if(a==b);
        else
        {
          set.get(a).addAll(set.get(b));
          set.remove(set.get(b));
        }
    }
    static void addset(String bow,String arrow)
    {
        for(int i=0;i<set.size();i++)
        {
            if(set.get(i).contains(arrow))
            {
                set.get(i).add(bow);
            }
        }
    }
    static HashSet<String> getvariables(String e)
    {
        HashSet<String> hope=new HashSet<String>();
        for(int i=0;i<set.size();i++)
        {
            if(set.get(i).contains(e))
            return set.get(i);
        }
        
        return hope;
    }
    static boolean checkset(String s)
    {
        if(set.isEmpty())
        return false;
        for(int i=0;i<set.size();i++)
        {
            if(set.get(i).contains(s))
            return true;
        }
        return false;
    }
    static void modifyorder(Statement s)
    {
        int i=0;
        String temp=s.RHS.fname;
        for(i=0;i<s.LHS.size();i++)
        {
            if(temp.equals(s.LHS.get(i).fname))
                break;
        }
        if(i==s.LHS.size());
        else
        {
            Predicate x=s.LHS.remove(i);
            s.LHS.add(x);
        }
    }
    static boolean checkquery(String input)
    {
        String cname=null;
        if(input.contains("~"))
        cname=input.substring(0,2);
        else
        cname=input.substring(0,1);
        List<String> cvar=new ArrayList<>();
        String temp=input.substring(input.indexOf("(")+1,input.indexOf(")"));
        String tempi[]=temp.split(",");
        Collections.addAll(cvar, tempi);
        
        for(int k=0;k<state.size();k++)
        {
            state.get(k).print();
        }
        for(int i=0;i<state.size();i++)
        {
            if(state.get(i).factOrFunction)
            {
                if(state.get(i).fact.fname.equals(cname))
                {
                    if(cvar.equals(state.get(i).fact.var1))
                        return true;
                }
            }
            else
            {
                
                if(state.get(i).RHS.fname.equals(cname))
                {
                    if(cvar.size()==state.get(i).RHS.var1.size())
                    {
                        
                    }
                }
            }
            
            
        }
        
        
        return false;
    }
    static Statement standardize(Statement state)
    {
       HashMap<String,String> mapping;
       List<Predicate> left=new ArrayList<Predicate>(state.LHS);
       List<String> right=new ArrayList<String>(state.RHS.var1);
       //System.out.println(right.toString());
       //for(int i=0;i<state.size();i++)
       //{
           //if(state.get(i).factOrFunction==false)
           //{
       
                mapping=new HashMap<String,String>();
                //left=new ArrayList<String>(state.get(i).RHS.var1);
                boolean checkstatement=check(state);
                //if(checkstatement==false)
                //{
                    for(int j=0;j<right.size();j++)
                    {
                        if(Character.isUpperCase(right.get(j).charAt(0)));
                        else
                        {
                                if(!mapping.containsKey(right.get(j)))
                            {
                                mapping.put(right.get(j),"a".concat(""+counter++));
                    //System.out.println(variable.get(j)+" check ");
                    //System.out.println(h.get(variable.get(j)));
                                right.set(j,mapping.get(right.get(j)));
                            }
                            else
                            right.set(j,mapping.get(right.get(j)));
                    //System.out.println(variable.get(j));
                        }
                    }
                //List<Predicate> tempfunction=new ArrayList<Predicate>(state.get(i).LHS);
                //for(int k=0;k<tempfunction.size();k++)
                //{
                    //right=new ArrayList<String>(tempfunction.get(k).var1);
                    for(int l=0;l<left.size();l++)
                    {
                        for(int ip=0;ip<left.get(l).var1.size();ip++)
                        {
                            if(Character.isUpperCase(left.get(l).var1.get(ip).charAt(0)));
                            else
                            {
                                if(mapping.containsKey(left.get(l).var1.get(ip)))
                                left.get(l).var1.set(ip,mapping.get(left.get(l).var1.get(ip)));
                                else
                                {
                                    mapping.put(left.get(l).var1.get(ip),"a".concat(""+counter++));
                                    left.get(l).var1.set(ip,mapping.get(left.get(l).var1.get(ip)));
                                }
                            }
                        }
                        
                    }
                    //state.get(i).LHS.get(k).var1=new ArrayList<String>(right);
                    
                    
                //}
                for(Map.Entry<String, String> entry : mapping.entrySet())
                {
                    //System.out.println(entry.getKey()+" - "+entry.getValue());
                }
                //state.get(i).RHS.var1=new ArrayList<String>(left);
                
                
                
           //}
           //else;
       //}
       
    //}
                //}
                state.LHS=left;
                state.RHS.var1=right;
                return state;
                

    
    
    
    }
    
    static boolean check(Statement state)
    { 
        boolean checking=true;
        List<String> list1=new ArrayList<String>();
        for(int i=0;i<state.LHS.size();i++)
        {
            for(int j=0;j<state.LHS.get(i).var1.size();j++)
            {
                if(!list1.contains(state.LHS.get(i).var1.get(j)))
                list1.add(state.LHS.get(i).var1.get(j));
                else
                return false;
            }
        }
        for(int k=0;k<state.RHS.var1.size();k++)
        {
            if(!list1.contains(state.RHS.var1.get(k)))
            list1.add(state.RHS.var1.get(k));
            else
            return false;
        }
        return checking;
    }
}
/*
QUERY G(Add) 

OR G(Add) {} 

ADDING TO VISITED  G(Add)
RULES [F(x,y,z) ^ R(z) => G(z)] 1
AND ['F(x0,y0,z0)', 'R(z0)'] {'z0': 'Add'} 

a1
a2
OR F(x0,y0,Add) {'z0': 'Add'} 

RULES [A(x,y,z) => F(x,y,x)] 1
AND ['A(x1,y1,z1)'] {'y1': 'y0', 'x1': 'Add', 'z0': 'Add'} 

a1
a2
OR A(Add,y0,z1) {'y1': 'y0', 'x1': 'Add', 'z0': 'Add'} 

FROM_FACT {'y0': 'Sub', 'z1': 'Add'} 

AND_theta' {'y1': 'y0', 'y0': 'Sub', 'x1': 'Add', 'z0': 'Add', 'z1': 'Add'} 

AND_theta' (No rest terms) {'y1': 'y0', 'y0': 'Sub', 'x1': 'Add', 'z0': 'Add', 'z1': 'Add'} 

OR_VAL {'y1': 'y0', 'y0': 'Sub', 'x1': 'Add', 'z0': 'Add', 'z1': 'Add'} 

AND_theta' {'y1': 'y0', 'y0': 'Sub', 'x1': 'Add', 'z0': 'Add', 'z1': 'Add'} 

AND ['R(z0)'] {'y1': 'y0', 'y0': 'Sub', 'x1': 'Add', 'z0': 'Add', 'z1': 'Add'} 

a1
a2
OR R(Add) {'y1': 'y0', 'y0': 'Sub', 'x1': 'Add', 'z0': 'Add', 'z1': 'Add'} 

FROM_FACT {} 

AND_theta' {'y1': 'y0', 'y0': 'Sub', 'x1': 'Add', 'z0': 'Add', 'z1': 'Add'} 

AND_theta' (No rest terms) {'y1': 'y0', 'y0': 'Sub', 'x1': 'Add', 'z0': 'Add', 'z1': 'Add'} 

AND_theta'' {'y1': 'y0', 'y0': 'Sub', 'x1': 'Add', 'z0': 'Add', 'z1': 'Add'} 

OR_VAL {'y1': 'y0', 'y0': 'Sub', 'x1': 'Add', 'z0': 'Add', 'z1': 'Add'} 

{'y1': 'y0', 'y0': 'Sub', 'x1': 'Add', 'z0': 'Add', 'z1': 'Add'}
ANSWER {'y1': 'y0', 'y0': 'Sub', 'x1': 'Add', 'z0': 'Add', 'z1': 'Add'}
G(Add) TRUE
*/
