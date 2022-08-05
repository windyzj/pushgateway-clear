package com.atguigu.prome;

import com.atguigu.common.HttpUtil;

import java.math.BigDecimal;
import java.util.Date;

public class PushgatewayClear {



    static String pushGatewayURL="http://hadoop102:9091";
    static String metricsURL=pushGatewayURL+"/metrics";
    static String deleteURL=pushGatewayURL+"/metrics/job/";


    public static void main(String[] args) throws Exception{
          String hostAndPort= args[0];
          String expireSecStr= args[1];
          String runType= args[2];

           pushGatewayURL="http://"+hostAndPort;

           Integer  expireSec= Integer.valueOf(expireSecStr);
           metricsURL=pushGatewayURL+"/metrics";
           deleteURL=pushGatewayURL+"/metrics/job/";
      while (true){
          String metricsStr = HttpUtil.get(metricsURL);

          String[] stringLines = metricsStr.split("\\n");
          for (String line : stringLines) {
              if(line.startsWith("push_time_seconds")){
                  checkAndDelete(  line,expireSec);
              }
          }

          if(!runType.equals("continue")){
              break;
          }
          Thread.sleep(expireSec*1000L);

      }



    }

    static void  checkAndDelete(String line,Integer expireSec){
        int jobInfoStartIdx = line.indexOf("{");
        int jobInfoEndIdx = line.indexOf("}");


        String json = line.substring(jobInfoStartIdx, jobInfoEndIdx+1);
        String tsString= line.substring(jobInfoEndIdx+1  ).trim().replace("+", "");




        Long expireTs = (new BigDecimal(tsString).intValue()+ expireSec  )  *1000L;
        Date expireDate = new Date(expireTs );

        Date nowDate = new Date();

        if(nowDate.compareTo(expireDate)>0){
            int jobIdStartIdx = json.indexOf("job=")+5;
            int jobIdEndIdx= json.indexOf("\"", jobIdStartIdx+1);
            String jobId = json.substring(jobIdStartIdx, jobIdEndIdx);
            String deleteJobURL=deleteURL+jobId;
            System.out.println("---delete job_id= "+jobId+"----");
            HttpUtil.delete(deleteJobURL);
        }
    }



}
