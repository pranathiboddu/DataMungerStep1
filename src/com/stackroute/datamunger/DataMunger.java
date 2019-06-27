package com.stackroute.datamunger;

/*There are total 5 DataMungertest files:
 * 
 * 1)DataMungerTestTask1.java file is for testing following 3 methods
 * a)getSplitStrings()  b) getFileName()  c) getBaseQuery()
 * 
 * Once you implement the above 3 methods,run DataMungerTestTask1.java
 * 
 * 2)DataMungerTestTask2.java file is for testing following 3 methods
 * a)getFields() b) getConditionsPartQuery() c) getConditions()
 * 
 * Once you implement the above 3 methods,run DataMungerTestTask2.java
 * 
 * 3)DataMungerTestTask3.java file is for testing following 2 methods
 * a)getLogicalOperators() b) getOrderByFields()
 * 
 * Once you implement the above 2 methods,run DataMungerTestTask3.java
 * 
 * 4)DataMungerTestTask4.java file is for testing following 2 methods
 * a)getGroupByFields()  b) getAggregateFunctions()
 * 
 * Once you implement the above 2 methods,run DataMungerTestTask4.java
 * 
 * Once you implement all the methods run DataMungerTest.java.This test case consist of all
 * the test cases together.
 */

public class DataMunger {

    /*
     * This method will split the query string based on space into an array of words
     * and display it on console
     */

    public String[] getSplitStrings(String queryString) {

        return queryString.toLowerCase().trim().split(" ");
    }

    /*
     * Extract the name of the file from the query. File name can be found after a
     * space after "from" clause. Note: ----- CSV file can contain a field that
     * contains from as a part of the column name. For eg: from_date,from_hrs etc.
     *
     * Please consider this while extracting the file name in this method.
     */

    public String getFileName(String queryString) {

        int index = queryString.indexOf("from");
        int index1 = queryString.indexOf(".");
        String result = queryString.substring(index + 5, index1 + 4);
        return result;


    }

    /*
     * This method is used to extract the baseQuery from the query string. BaseQuery
     * contains from the beginning of the query till the where clause
     *
     * Note: ------- 1. The query might not contain where clause but contain order
     * by or group by clause 2. The query might not contain where, order by or group
     * by clause 3. The query might not contain where, but can contain both group by
     * and order by clause
     */

    public String getBaseQuery(String queryString) {
        int index = queryString.indexOf(" where ");
        int index1 = queryString.indexOf("group by");
        if (index != -1) {
            String result = queryString.substring(0, index);
            return result;
        } else if (index1 != -1) {
            String result = queryString.substring(0, index1);
            return result.trim();
        } else {
            return queryString;
        }
    }



    /*
     * This method will extract the fields to be selected from the query string. The
     * query string can have multiple fields separated by comma. The extracted
     * fields will be stored in a String array which is to be printed in console as
     * well as to be returned by the method
     *
     * Note: 1. The field name or value in the condition can contain keywords
     * as a substring. For eg: from_city,job_order_no,group_no etc. 2. The field
     * name can contain '*'
     *
     */

    public String[] getFields(String queryString) {
        int index = queryString.indexOf("select");
        int index1 = queryString.indexOf("from");
        String result = queryString.substring(index + 7, index1 - 1);
        String[] fields = result.split(",");
        return fields;

        // queryString = queryString.toLowerCase();
        //String [] array = queryString.split(" ")[1].split(",");
        //return array;
    }

    /*
     * This method is used to extract the conditions part from the query string. The
     * conditions part contains starting from where keyword till the next keyword,
     * which is either group by or order by clause. In case of absence of both group
     * by and order by clause, it will contain till the end of the query string.
     * Note:  1. The field name or value in the condition can contain keywords
     * as a substring. For eg: from_city,job_order_no,group_no etc. 2. The query
     * might not contain where clause at all.
     */

    public String getConditionsPartQuery(String queryString) {
        String afterWhere = "";
        int pos = queryString.indexOf("where ");
        int ord = queryString.indexOf("order by");
        int grp = queryString.indexOf("group by");
        if(pos == -1) {
            return null;
        }
        else {
            if ((ord == -1) && (grp == -1)) {
                afterWhere = queryString.substring(pos + 6, queryString.length());
            } else if (grp > 0) {
                afterWhere = queryString.substring(pos+6, grp);
            } else {
                afterWhere = queryString.substring(pos+6, ord);
            }
        }
        return afterWhere.toLowerCase().trim();
        }


    /*
     * This method will extract condition(s) from the query string. The query can
     * contain one or multiple conditions. In case of multiple conditions, the
     * conditions will be separated by AND/OR keywords. for eg: Input: select
     * city,winner,player_match from ipl.csv where season > 2014 and city
     * ='Bangalore'
     *
     * This method will return a string array ["season > 2014","city ='bangalore'"]
     * and print the array
     *
     * Note: ----- 1. The field name or value in the condition can contain keywords
     * as a substring. For eg: from_city,job_order_no,group_no etc. 2. The query
     * might not contain where clause at all.
     */

    public String[] getConditions(String queryString) {

        int index = queryString.indexOf("where");
        if (index == -1) {
            return null;
        } else {
            queryString = queryString.toLowerCase();
            String[] array = queryString.split("where")[1].trim().split("group by | order by")[0].trim().split(" and | or ");
            return array;
        }
    }

    /*
     * This method will extract logical operators(AND/OR) from the query string. The
     * extracted logical operators will be stored in a String array which will be
     * returned by the method and the same will be printed Note:  1. AND/OR
     * keyword will exist in the query only if where conditions exists and it
     * contains multiple conditions. 2. AND/OR can exist as a substring in the
     * conditions as well. For eg: name='Alexander',color='Red' etc. Please consider
     * these as well when extracting the logical operators.
     *
     */

    public String[] getLogicalOperators(String queryString) {

        queryString = queryString.toLowerCase();
        String[] query = queryString.split(" ");
        String logical = "";
        if (queryString.contains("where ")) {
            for (int i = 0; i < query.length; i++) {
                if (query[i].matches("and|or|not")) {

                    logical += query[i] + " ";
                }
            }
            return logical.toString().split(" ");
        }
        return null;

    }

    /*
     * This method extracts the order by fields from the query string. Note:
     * 1. The query string can contain more than one order by fields. 2. The query
     * string might not contain order by clause at all. 3. The field names,condition
     * values might contain "order" as a substring. For eg:order_number,job_order
     * Consider this while extracting the order by fields
     */

    public String[] getOrderByFields(String queryString) {
        int index = queryString.indexOf("order by");
        if (index != -1) {
            String substr = queryString.substring(index + 8);
            String[] result = substr.trim().split(",");
            return result;
        } else {
            return null;
        }

    }



    /*
     * This method extracts the group by fields from the query string. Note:
     * 1. The query string can contain more than one group by fields. 2. The query
     * string might not contain group by clause at all. 3. The field names,condition
     * values might contain "group" as a substring. For eg: newsgroup_name
     *
     * Consider this while extracting the group by fields
     */

    public String[] getGroupByFields(String queryString) {
        int index = queryString.indexOf("group by");
        if (index != -1) {
            String substr = queryString.substring(index + 8);
            String[] result = substr.trim().split(",");
            return result;
        } else {
            return null;
        }


    }

    /*
     * This method extracts the aggregate functions from the query string. Note:
     *  1. aggregate functions will start with "sum"/"count"/"min"/"max"/"avg"
     * followed by "(" 2. The field names might
     * contain"sum"/"count"/"min"/"max"/"avg" as a substring. For eg:
     * account_number,consumed_qty,nominee_name
     *
     * Consider this while extracting the aggregate functions
     */

    public String[] getAggregateFunctions(String queryString) {
        queryString=queryString.replace(","," ");
        String arr[]=queryString.split(" ");
        int i=0;
        String[] result=new String[arr.length];
        for(String str:arr) {
            if(str.contains("sum") || str.contains("count") ||str.contains("max") ||str.contains("min")||str.contains("avg"))
            {
                result[i]=str;
                i++;
            }
        }
        String[] finalresult=new String[i];
        for(int j = 0;j < i; j++)
        {
            finalresult[j]=result[j];
        }
        if(i == 0)
        {
            return null;
        }
    return finalresult;
    }
}



