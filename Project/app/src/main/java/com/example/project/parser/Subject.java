package com.example.project.parser;
/**
 * @author u7726399 Meitong Liu
 */
//remove Space' ' and '&'
public enum Subject{
    internationalrelations,management,collegeofbusinessandeconomicsadministration,biology,facultyofscience,healthscience,
    history,physics,business,informationsystems,visualarts,asianstudies,actuarialstudies,statistics,english,laws,linguistics,
    mathematics,computerscience,philosophy,greek,psychology,arabic,economics,econometrics,engineering,financialmanagement,
    archaeology,french,persian,indonesian,italian,japanese,music,korean,latin,chinese,chemistry,classics,sociology,spanish,
    arts,advstudiesinthesocialsciencesandhumanities,socialresearch,biologicalanthropology,marketing,arthistory,
    environmentalscience,internationalanddevelopmentaleconomics,criminology,strategicstudies,populationhealth,
    earthandmarinescience,culture,healthandmedicine,anthropology,cecsexperimental,interdisciplinary,cybernetics,
    crawfordschoolofpublicpolicy,politicalscience,
    marketingandinternationalbusiness,environmentalmanagementdevelopment,/*&*/
    astronomyandastrophysics,pacificstudies,asiapacificaffairs,screenstudies,economichistory,indigenousstudies,
    australiannationalinternship,nationalsecuritypolicy,middleeasterncentralasianstudies/*&*/,
    design,humanities,vicechancellorundergraduate,vicechancellorpostgraduate,medicalscience,regnet,policyandgovernance,
    burmese,neuroscience,museumandcollection,sciencecommunication,europeanstudies,diplomacy,portuguese,demography,turkish,
    genderstudies,finalhonoursgrade,german,hindi,russian,medicine,mongolianlanguage,politics,philosophyandeconomics,
    chinesestudiesinlanguage,populationmentalhealthresearch,languagestudies,sanskrit,warstudies,tetum,thai,thesis,tibetan,
    tokpisin,vietnamese;

    public static Subject getSubject(String str) {
        String subjectstr= str.replaceAll("[^A-Za-z]","");
        try {
            return Subject.valueOf(subjectstr.toLowerCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

}
