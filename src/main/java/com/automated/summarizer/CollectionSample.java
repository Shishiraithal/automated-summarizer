package com.automated.summarizer;

import java.io.*;
import java.util.*;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.tartarus.martin.Stemmer;



public class CollectionSample {
    
       
       public static ArrayList<String> unordered_sent = new ArrayList();
       public static ArrayList<String> ordered_sent = new ArrayList();
       
       public static Map<String, Integer> unorderedMap = new TreeMap(String.CASE_INSENSITIVE_ORDER);
       public static Map<String, Integer> orderedMap = new TreeMap(String.CASE_INSENSITIVE_ORDER);
       
       public static String all_stopwords=null,all_cuewords=null,combined_summary=" ";
       public static String []all_summaries=new String[Util.file_no];
       public static String []all_files=new String[Util.file_no];
       public static int summaryPercentage=Util.summaryPercent;
       public static int argMax=0,prefUV=0,prefVU=0;
       
       public static long startTime,endTime;
       public static boolean isStopWord(String word,List<String> stop_word_list)
       {
           return stop_word_list.contains(word);
       }
       public static String stemWord(String word)
       {
           if(Util.isParallel!=true)
           {
           Stemmer myStemmer=new Stemmer();
           String stemmedWord="";
           
           char []ch=word.toCharArray();
           
           myStemmer.add(ch, ch.length);
           myStemmer.stem();
           stemmedWord=myStemmer.toString();
           
           return stemmedWord;
           }
           else{
              
               ThreadLocal<String> stemmedWord=new ThreadLocal<String>();
              Stemmer myStemmer2=new Stemmer();
               char []ch=word.toCharArray();
               myStemmer2.add(ch, ch.length);
               myStemmer2.stem();
               stemmedWord.set(myStemmer2.toString());
               String retValue=stemmedWord.get();
               stemmedWord.remove();
               return retValue;
               }
       }
       
       public static void load_stopwords()
       {
           try {
              /* 
               FileInputStream fi = null;
               File f;
               f=new File("stopwords.txt");
               fi = new FileInputStream(f);
               byte []b=new byte[(int)f.length()];
               fi.read(b);
               */
        ////////////Here b=contents of stopwords.txt/////////////////
               String stop="a,able,about,across,after,all,almost,also,am,among,an,and,any,are,as,at,be,because,been,but,by,can,cannot,could,dear,did,do,does,either,else,ever,every,for,from,get,got,had,has,have,he,her,hers,him,his,how,however,i,if,in,into,is,it,its,just,least,let,like,likely,may,me,might,most,must,my,neither,no,nor,not,of,off,often,on,only,or,other,our,own,rather,said,say,says,she,should,since,so,some,than,that,the,their,them,then,there,these,they,this,tis,to,too,twas,us,wants,was,we,were,what,when,where,which,while,who,whom,why,will,with,would,yet,you,your";
              
               all_stopwords=stop;
              /* f=new File("cuewords.txt");
               fi = new FileInputStream(f);
               b=new byte[(int)f.length()];
               fi.read(b);
               */
         ////////////Here b=contents of cuewords.txt/////////////////
               String cue="incidentally,for example,anyway,by the way,furthermore,first,second,then,now,thus,moreover,therefore,hence,lastly,finally,in summary,on the other hand,because,when,however";
               all_cuewords=cue;
               //fi.close();
           } catch (Exception ex) {
               Logger.getLogger(CollectionSample.class.getName()).log(Level.SEVERE, null, ex);
           }
           
       }
       
       public static String load_file(int fileNo)
       {
           
           //FileInputStream fi = null;
           String f=null;
        try {
            // Loading Respective files
            switch(fileNo)
            {
                case 1 :  f=Util.url1;
                          break; 
                case 2:   f=Util.url2;
                          break;
               /*
                case 3:   f=new String(Util.url3);
                          break; 
                case 4:   f=new String(Util.url4);
                          break; 
                case 5:   f=new String(Util.url5);
                          break; 
                case 6:   f=new String(Util.url6);
                          break; 
                case 7:   f=new String(Util.url7);
                          break; 
                default:  f=new String(Util.url8);
                          break;
                          */
            }  
                          
           /* 
            fi = new FileInputStream(f);
            byte[] b=new byte[(int)f.length()];
            fi.read(b);
            */
            String b=f;
            ///////////////Declare and Change b to text to be input///////////////
            String str1=b;            
            all_files[fileNo-1]=str1;
            String str2=str1.replace('.', '#');
            //fi.close();
            return str2;
       }   catch (Exception ex) {
               Logger.getLogger(CollectionSample.class.getName()).log(Level.SEVERE, null, ex);
               return null;
           }
       
       }
       
       public static void invoke()
       {
           startTime=System.currentTimeMillis();
           load_stopwords();
          
            if(Util.isParallel==true)  
            {  
                MyThread[] mt = new MyThread[Util.file_no];  /////////////for our project file_no=4////////////
                for(int i=0;i<Util.file_no;i++)
                {

                    mt[i]=new MyThread(i+1);
                    mt[i].start();
                }
                    for(int j=0;j<Util.file_no;j++)
                    {
                        try {
                            mt[j].join();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(CollectionSample.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                
            }
            else
            {
                for(int i=0;i<Util.file_no;i++)
                    summarize(i+1);
            }
           combine_summary();
          endTime=System.currentTimeMillis();
          long totalTime=endTime-startTime;
          Util.totalTime=totalTime;
          //System.out.println("\n\nTime Consumed : "+totalTime);
          
           
       }
       
       public static void summarize(int no_of_files)
       {
           
           String file_contents = null,final_summary = null;
           int noOfSentencesInSummary = 0;
           ArrayList<String> sent_list = new ArrayList();
           ArrayList<String> all_words = new ArrayList();
           ArrayList<String> stop_word_list = new ArrayList();
           Map<String, Integer> freqMap = new TreeMap(String.CASE_INSENSITIVE_ORDER);
           Map<String, Float> sentMap = new TreeMap(String.CASE_INSENSITIVE_ORDER);
           ArrayList<String> list_summary_final = new ArrayList();
           ArrayList<String> summary_list = new ArrayList();

           
           
           //while(no_of_files<=Util.file_no)
           {
           /*    
           String fileName="summary"+no_of_files+".txt";
           //PrintWriter pw=null;
           //FileOutputStream fout=null;
            File f1=new File(fileName);
           try {          
             //  fout=new FileOutputStream(f1);
            //   pw = new PrintWriter(f1);
           } catch (FileNotFoundException ex) {
               Logger.getLogger(CollectionSample.class.getName()).log(Level.SEVERE, null, ex);
           }
           
           */
                   String temp1;
           
          
        file_contents=load_file(no_of_files);//file_contents;
        
        String str2=file_contents;
        String [] sent=str2.split("[#?!]");        // Stroing as array list
        sent_list.addAll(Arrays.asList(sent));
        //System.out.println(sent_list);
           
        temp1=file_contents;
        
        String []words = temp1.split("[ \\t\\n,\\\\.\\\"!?$~()\\\\[\\\\]\\\\{\\\\}:;/\\\\\\\\<>+=%*#]");
        all_words.addAll(Arrays.asList(words));
        
        String []stopword = all_stopwords.split(",");
        
        stop_word_list.addAll(Arrays.asList(stopword));
        
       // System.out.println(stop_word_list);
        all_words.removeAll(stop_word_list);    // Removal of stopwords from the list
        //System.out.println(sent_list);
        int i=0;
        while(i<all_words.size())
        {
            
            String temp=all_words.get(i);
            String stemmedWord = stemWord(temp);
            
            if(freqMap.containsKey(stemmedWord))
            {
                int cnt =freqMap.get(stemmedWord);
                cnt++;
                freqMap.put(stemmedWord, cnt);
            }
            else
            {
                freqMap.put(stemmedWord, 1);
            }
            i++;
        }
        
         //System.out.println("------- Hash map-------\n"+freqMap);
         freqMap.remove("");
       // Sentence Weight calculation
       
       for(i=0;i<sent_list.size();i++)
       {
           float sent_wt=0;
           String sss="";
           String tempSentence=sent_list.get(i);
           int no_of_words=0;
           String []tempWords=tempSentence.split("[ \\t\\n,\\\\.\\\"!?$~()\\\\[\\\\]\\\\{\\\\}:;/\\\\\\\\<>+=%*#]");
           for(int j=0;j<tempWords.length;j++)
           {
               String temp2=tempWords[j];
               if(!isStopWord(temp2,stop_word_list)&&!temp2.equals("")&&!temp2.equals(null))
               {
                  no_of_words++;
                  sss=stemWord(temp2);
        
                  int wordfreq=freqMap.get(sss);
                  
                  sent_wt+=wordfreq;
                  
               }
           }
                            // Normalizing the weight of the sentence
           float normalizedWeight=sent_wt/no_of_words;
                                                  // Increasing the sentence weight if CUE WORD exists
            String []cue_words=all_cuewords.split(",");
           for(int m=0;m<cue_words.length;m++)
           {
        
               if(tempSentence.contains(cue_words[m]))
               {
                   //System.out.println("\n------Contains cue Phrase---- "+cue_words[m]+" "+normalizedWeight);
                   normalizedWeight+=3;
               }
           }
           //System.out.println(tempSentence+"  "+(normalizedWeight));
           sentMap.put(tempSentence, normalizedWeight);
           
           
       }
                            // Sorting The Map
       Map<String,Float> sortedMap=sortMapByValue(sentMap);
          //System.out.println("...........After Sorting..........\n\n"+sortedMap);
       
          
                        // Calculating the no of sentences based on summary %
          
          noOfSentencesInSummary=(sortedMap.size()*summaryPercentage)/100;
          
          summary_list.addAll(sortedMap.keySet());
         
          final_summary=" ";
          
          int cnt=0;
          while(cnt<noOfSentencesInSummary)
          {
              list_summary_final.add(summary_list.get(cnt));
              cnt++;
          }
          
         // System.out.println("...........selected summary..........\n\n"+list_summary_final);
          cnt=0;
          for(String s : sent_list  )
          {
              
              if(cnt<=noOfSentencesInSummary)
              {
    //               //System.out.println(s);
                if(list_summary_final.contains(s))
                {
                    //System.out.println(s);
                    final_summary+=s+".";
                    cnt++;
                }
              }
              else
              {
                  break;
              }
              
              
          }
         // System.out.println("...........Final Summary.........."+noOfSentencesInSummary+"\n\n"+final_summary);
         /*
          pw.write(final_summary);
          pw.flush();
    
          pw.close();
          */
          all_summaries[no_of_files-1]=final_summary;
          no_of_files++;
          
          final_summary="";
          all_words.removeAll(all_words);
          sent_list.removeAll(sent_list);
          summary_list.removeAll(summary_list);
          list_summary_final.removeAll(list_summary_final);
          
           }
           
        }
       
       
       
       public static void summarizeParallel(ThreadLocal<Integer> no_of_files,ThreadLocal<String> file_contents,ThreadLocal<String> final_summary,ThreadLocal<Integer> noOfSentencesInSummary 
               , ThreadLocal<ArrayList<String>> sent_list, ThreadLocal<ArrayList<String>> all_words, ThreadLocal<ArrayList<String>> stop_word_list, ThreadLocal<TreeMap<String,Integer>> freqMap
               ,ThreadLocal<TreeMap<String, Float>> sentMap, ThreadLocal<ArrayList<String>> list_summary_final, ThreadLocal<ArrayList<String>> summary_list)
       {
           
        ArrayList<String> allWordsTempList=new ArrayList<String>();   
        ArrayList<String> tempList=new ArrayList<String>();   
        ArrayList<String> tempList2=new ArrayList<String>();   
        ArrayList<String> stopWordsTempList=new ArrayList<String>();   
           {
          /*     
           String fileName="Parallelsummary"+no_of_files.get()+".txt";
           //PrintWriter pw=null;
           File f1=new File(fileName);
           try {          
        
           //    pw = new PrintWriter(f1);
           } catch (FileNotFoundException ex) {
               Logger.getLogger(CollectionSample.class.getName()).log(Level.SEVERE, null, ex);
           }
          */     
           String temp1;
           int num=no_of_files.get();
          temp1=load_file(num);
        
            file_contents.set(temp1);//file_contents;
        
        String str2=file_contents.get();
        String [] sent=str2.split("[#?!]");        // Stroing as array list
        tempList.addAll(Arrays.asList(sent));
        sent_list.set(tempList);
        
           
        temp1=file_contents.get();
        
        String []words = temp1.split("[ \\t\\n,\\\\.\\\"!?$~()\\\\[\\\\]\\\\{\\\\}:;/\\\\\\\\<>+=%*#]");
        allWordsTempList.clear();
        allWordsTempList.addAll(Arrays.asList(words));
        all_words.set(allWordsTempList);
        
        
        String []stopword = all_stopwords.split(",");
        
        stopWordsTempList.addAll(Arrays.asList(stopword));
        
        stop_word_list.set(stopWordsTempList);
        tempList2.addAll(all_words.get());
        
        tempList2.removeAll(stop_word_list.get());
        
       all_words.get().remove("");
        all_words.set(tempList2);
        int i=0;
        TreeMap<String,Integer> tempFreqMap=new TreeMap<String,Integer>();
        while(i<all_words.get().size())
        {
            ThreadLocal<String> temp=new ThreadLocal<String>();
            ThreadLocal<String> stemmedWord=new ThreadLocal<String>();
           
            
            temp.set(all_words.get().get(i));
            stemmedWord.set(stemWord(temp.get()));
            if(tempFreqMap.containsKey(stemmedWord.get()))
            {
                int cnt =tempFreqMap.get(stemmedWord.get());
                cnt++;
                
                tempFreqMap.put(stemmedWord.get(), cnt);

            }
            else
            {
                tempFreqMap.put(stemmedWord.get(), 1);

            }
            i++;
            temp.remove();
            stemmedWord.remove();
        }
        
        freqMap.set(tempFreqMap);

        freqMap.get().remove("");    // Remove null char from hashmap
        TreeMap<String,Float> tempSentMap=new TreeMap<String,Float>();
       for(i=0;i<sent_list.get().size();i++)
       {
           float sent_wt=0;
           String sss="";
           String tempSentence=sent_list.get().get(i);
           int no_of_words=0;
           String []tempWords=tempSentence.split("[ \\t\\n,\\\\.\\\"!?$~()\\\\[\\\\]\\\\{\\\\}:;/\\\\\\\\<>+=%*#]");
           for(int j=0;j<tempWords.length;j++)
           {
               String temp2=tempWords[j];
               if(!isStopWord(temp2,stop_word_list.get())&&!temp2.equals("")&&!temp2.equals(null))
               {
                  no_of_words++;
                  sss=stemWord(temp2);

                  int wordfreq=freqMap.get().get(sss);
                  
                  sent_wt+=wordfreq;
                  
               }
           }
           float normalizedWeight=sent_wt/no_of_words;
                                                  // Increasing the sentence weight if CUE WORD exists
            String []cue_words=all_cuewords.split(",");
           for(int m=0;m<cue_words.length;m++)
           {
        
               if(tempSentence.contains(cue_words[m]))
               {
  //                // System.out.println("\n------Contains cue Phrase---- "+cue_words[m]+" "+normalizedWeight);
                   normalizedWeight+=3;
               }
           }
//           //System.out.println(tempSentence+"  "+(normalizedWeight));
           tempSentMap.put(tempSentence, normalizedWeight);
           
       }
       tempSentMap.remove("");
       sentMap.set(tempSentMap);
                            // Sorting The Map
       Map<String,Float> sortedMap=sortMapByValue(sentMap.get());
          //System.out.println("...........After Sorting..........\n\n"+sortedMap);
       
          
                        // Calculating the no of sentences based on summary %
          ArrayList<String> temp_summary_list=new ArrayList<String>();
          ArrayList<String> temp_list_summary_final=new ArrayList<String>();
          noOfSentencesInSummary.set((sortedMap.size()*summaryPercentage)/100);

          temp_summary_list.addAll(sortedMap.keySet());

         
          final_summary.set(" ");
          
          int cnt=0;
          int last_val=noOfSentencesInSummary.get();
          while(cnt<last_val)
          {
              String t1=temp_summary_list.get(cnt);
              temp_list_summary_final.add(t1);
              cnt++;
          }
          summary_list.set(temp_summary_list);
          list_summary_final.set(temp_list_summary_final);

          cnt=0;
          for(String s : sent_list.get()  )
          {
              
              if(cnt<=last_val)
              {

                if(list_summary_final.get().contains(s))
                {
                    String s1=s+".";
                    String s2=final_summary.get();
                    final_summary.set(s2+s1);
                    cnt++;
                }
              }
              else
              {
                  break;
              }
              
              
          }
         // System.out.println("...........Final Summary.........."+noOfSentencesInSummary.get()+"\n\n"+final_summary.get());
         /*  
          pw.write(final_summary.get());
          pw.flush();

          pw.close();
          
          */
          all_summaries[no_of_files.get()-1]=final_summary.get();
          no_of_files.set(no_of_files.get()+1);
          
          final_summary.set(" ");
          all_words.get().removeAll(all_words.get());
          sent_list.get().removeAll(sent_list.get());
          summary_list.get().removeAll(summary_list.get());
          list_summary_final.get().removeAll(list_summary_final.get());
          
           }
           
        }
       
       
       //Combining the summaries
    public static void combine_summary() {
        load_summaries();
        
       // PrintWriter pw=null;
        File f1=null;
        if(Util.isParallel!=true)
        {
            f1=new File("combinedSummary.txt");
        } 
        else
        {
            f1=new File("combinedSummaryParallel.txt");
        }
        if(Util.file_no==1)
        {
           // try {
                combined_summary=all_summaries[0];
               
                //pw = new PrintWriter(f1);
                //pw.write(combined_summary);
                //pw.close();
                
                Util.combinedSummary=combined_summary;
                //Util.combinedSummaryPath=f1.getAbsolutePath();
                
            //} catch (FileNotFoundException ex) {
                //Logger.getLogger(CollectionSample.class.getName()).log(Level.SEVERE, null, ex);
            }
        //}
        else
        {
        boolean similar=check_similarity();  // check similarity of documents
       /* 
        if(similar==false)
        {
            JOptionPane.showMessageDialog(null, "The documents are not Related !! \n Cannot combine the summaries !!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        */
        calculate_preference_values("");    // Calculate the values for the first time
                    // Sort in decending order to get the highest rank
        unorderedMap=sortMapByValue(unorderedMap);
        
        while(!unorderedMap.isEmpty())
        {
            String key=null;
            int val=0;
            argMax=unorderedMap.size();
          for(Map.Entry entry : unorderedMap.entrySet())
          {
              key=(String)entry.getKey();
              val=(Integer)entry.getValue();
              orderedMap.put(key, val);
               break;
          }  
           
           calculate_preference_values(key);    // Calculate w.r.t. a particular sentence
           unorderedMap.remove(key);            // Remove the key 
           unordered_sent.remove(key);
           
        }
        //System.out.println("\n\n-------------- Combined Summary -----------+ \n\n");
        orderedMap=sortMapByValue(orderedMap);
      
          // try {          
               // pw = new PrintWriter(f1);
                for(Map.Entry entry : orderedMap.entrySet())
                {
                    String sent1=(String)entry.getKey();
                   // System.out.println(sent1+" -->"+entry.getValue());
                    combined_summary+=sent1;
                  // pw.write(sent1);
                }
          /* } catch (FileNotFoundException ex) {
              / Logger.getLogger(CollectionSample.class.getName()).log(Level.SEVERE, null, ex);
           }
          */
           Util.combinedSummary=combined_summary;
            //System.out.println("Combined Summary : \n\n"+Util.combinedSummary);
           //Util.combinedSummaryPath=f1.getAbsolutePath();
           //pw.flush();
           //pw.close();
        }
    }
    
    public static void calculate_preference_values(String key)
    {   
        for(String sentence:unordered_sent)
        {
        
            List<String> sWords = new ArrayList();
            int i=0;
            String []words=splitSentence(sentence);
            for(String str:words)
            {
                sWords.add(stemWord(str));
            }
            
            lexical_stem_match(sWords,key);
            
            if(key=="")
                unorderedMap.put(sentence, prefUV-prefVU);
            else
            {
                int oldVal=unorderedMap.get(key);
                int newVal=oldVal+prefUV-prefVU;
                unorderedMap.put(key, newVal);
            }
            
            prefUV=prefVU=0;
        }
    }
    // Lexical stemming match with summary 
    public static void lexical_stem_match(List<String> u , String key)
    {
        
        if(key!="")
            calculate_uv(u,key);
        else
        {
             for(String sentence:unordered_sent)
            {
                calculate_uv(u,sentence);
            }
            
        }

        
    }
    
    public static void calculate_uv(List<String> u ,String sentence)
    {
        int count1=0,count2=0;
        ArrayList<String> tempList=new ArrayList();
        tempList.clear();
        String []words=splitSentence(sentence);

        tempList.addAll(Arrays.asList(words));

        for(String str:words)
        {
        String v=stemWord(str);
        if(u.contains(v))           /// u -v
            {
                count1++;
            }

        }
      int n1=count1;
      int n2=words.length;
      float val=n1/(float)n2;

      if(val>=0.5)
          prefUV++;
//      System.out.println("   u-v \n ----------------------- \n Count is : "+count1+"\n n2 = "+n2+"\n n1/n2="+val);


      for(String sss:u)
      {
        //String uu=stemWord(sss);
        if(tempList.contains(sss))               // v-u
        {
            count2++;
        }
      }

      n1=count2;
      n2=u.size();
      val = n1/ (float)n2;
      if(val>=0.5)
          prefVU++;

  //    System.out.println("   v-u \n -------- \nCount is : "+count2+"\n n2 = "+n2+"\n n1/n2="+val);
       count1=0;
       count2=0;

    }
    
    public static String[] splitSentence(String sentence)
    {
        return sentence.split("[ \\t\\n,\\\\.\\\"!?$~()\\\\[\\\\]\\\\{\\\\}:;/\\\\\\\\<>+=%*#]");
    }
       
       // Procedure to sort the Map in decending order
       public static Map sortMapByValue(Map unsortedMap)
       {
           List list = new LinkedList(unsortedMap.entrySet());
 
		// sort list based on comparator
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o2)).getValue())
                                       .compareTo(((Map.Entry) (o1)).getValue());
			}
		});
 
		// put sorted list into map again
                //LinkedHashMap make sure order in which keys were inserted
		Map sortedMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
       }
       
       
       
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code applicmation logic here
          
    }

    private static void load_summaries() {
        for(int i=0;i<Util.file_no;i++)
        {
            if(all_summaries[i]!=null)
            {
                String []sentences=all_summaries[i].split("\\.");
                
                for(int j=0;j<sentences.length;j++)
                sentences[j]+=".";           
                unordered_sent.addAll(Arrays.asList(sentences));
              }
        }
        
    }

    private static boolean check_similarity() {
        int i=0,j=0,percentage=0;
        for(i=0;i<(all_summaries.length)-1;i++)
            for(j=i+1;j<all_summaries.length;j++)
            {
                percentage=calculate_similarity(i,j);
                if(percentage<40)
                    return false;
            }
        
        return true;
    }

    private static int calculate_similarity(int index, int nextIndex) {
    int percent=0;
    int count1=0,count2=0;
    ArrayList<String> summary1=new ArrayList<>();
    ArrayList<String> stop_word_list=new ArrayList<>();
    ArrayList<String> summary2=new ArrayList<>();
    ArrayList<String> words1=new ArrayList<>();
    ArrayList<String> words2=new ArrayList<>();
    summary1.addAll(Arrays.asList(all_files[index].split("\\.?!")));
    summary2.addAll(Arrays.asList(all_files[nextIndex].split("\\.\\?!")));
    for(String sentence1: summary1)
    {
            words1.addAll(Arrays.asList(splitSentence(sentence1)));
            
    }
    for(String sentence2: summary2)
    {
            words2.addAll(Arrays.asList(splitSentence(sentence2)));
    }
    /*
    //System.out.println("\n\n---1---- For Lists "+(index+1)+" and "+(nextIndex+1)+" -------\n\n");
    //System.out.println(words1+"\n\n"+words2+"\n\n");
    for(String temp:words1)
    {
        System.out.print(temp+" ,");
    }
    //System.out.println("");
    for(String temp:words2)
    {
        //System.out.print(temp+" ,");
    }
    */
    String []stopword = all_stopwords.split(",");
    stop_word_list.addAll(Arrays.asList(stopword));
    words1.removeAll(stop_word_list);
    words2.removeAll(stop_word_list);
        
    for(String str2:words2)
    {
        if(words1.contains(str2))
        {
            count1++;
        }
    }
    for(String str1:words1)
    {
        if(words2.contains(str1))
        {
            count2++;
        }
    }
     
    int totalCount=count1+count2;
    int totalWords=words1.size()+words2.size();
    percent=(totalCount*100)/totalWords;
    //System.out.println("\n\n------- For Lists "+(index+1)+" and "+(nextIndex+1)+" -------\n\n");
    //System.out.println(words1+"\n\n"+words2+"\n\n");
    //System.out.println("count 1: "+count1+" count2: "+count2+" total words: "+totalWords);
    //System.out.println("Percentage is : "+percent);
    return percent;
    }
}


class MyThread extends Thread
{
    private static ThreadLocal<Integer> no_files=new ThreadLocal<Integer>();
    
    private static ThreadLocal<String> file_contents=new ThreadLocal<String>();
    private static ThreadLocal<String> final_summary=new ThreadLocal<String>();
    private static ThreadLocal<Integer> noOfSentencesInSummary=new ThreadLocal<Integer>();;
    private static ThreadLocal<ArrayList<String>> sent_list = new ThreadLocal<ArrayList<String>>();
    private static ThreadLocal<ArrayList<String>> all_words = new ThreadLocal<ArrayList<String>>();
    private static ThreadLocal<ArrayList<String>> stop_word_list = new ThreadLocal<ArrayList<String>>();
    private static ThreadLocal<TreeMap<String, Integer>> freqMap = new ThreadLocal<TreeMap<String,Integer>>();
    private static ThreadLocal<TreeMap<String, Float>> sentMap = new ThreadLocal<TreeMap<String,Float>>();
    private static ThreadLocal<ArrayList<String>> list_summary_final = new ThreadLocal<ArrayList<String>>();
    private static ThreadLocal<ArrayList<String>> summary_list = new ThreadLocal<ArrayList<String>>();
    int fileNumber;
    public MyThread(int val)
    {
        fileNumber=val;
    }
    public void cleanup()
    {
        no_files.remove();
        file_contents.remove();
        final_summary.remove();
        noOfSentencesInSummary.remove();
        sent_list.remove();
        all_words.remove();
        stop_word_list.remove();
        freqMap.remove();
        sentMap.remove();
        list_summary_final.remove();
        summary_list.remove();
    }
   public void run ()
   {
       no_files.set(fileNumber);
       CollectionSample.summarizeParallel(no_files,file_contents,final_summary,noOfSentencesInSummary
                ,sent_list,all_words,stop_word_list,freqMap,sentMap,list_summary_final,summary_list);
       cleanup();
       
       
   }
}