class Course:
    conveners = []
    def __init__(self, code, name, session, deliverMode,
                 unit):
        self.code = code;
        self.name = name;
        self.deliverMode = deliverMode;
        self.unit = unit;
        self.session = session;

    def addConvener(self, convener):
        self.conveners.append(convener)

    def getCourseUrl(self):
        return "https://programsandcourses.anu.edu.au/2024/course/"+self.code

    def addAttr(self, name, value):
        match name:
            case "Offered by":
                self.offeredBy = value
            case "ANU College":
                self.college = value
            case "Course subject":
                self.subject = value
            case "Academic career":
                self.career = value
            case "Course convener":
                self.conveners = value.strip().split('\n')
            case _:
                pass

    def __str__(self):
        res = ("\t<course>\n" + "\t\t<code>" + self.code +
               "</code>\n\t\t<name>" + self.name +"</name>\n\t\t" +
               "<deliverMode>" + self.deliverMode + "</deliverMode>\n\t\t" +
               "<session>" + self.session + "</session>\n\t\t" +
               "<subject>" + self.subject + "</subject>\n\t\t" +
               "<college>" + self.college + "</college>\n\t\t" +
               "<offeredBy>"+self.offeredBy+"</offeredBy>\n\t\t" +
               "<unit>"+str(self.unit)+"</unit>\n\t\t" +
               "<career>"+self.career+"</career>\n\t\t")
        if len(self.conveners) == 0:
            res += "<conveners/>\n"
        else:
            res += "<conveners>\n\t\t"
            for item in self.conveners:
                res += "\t<convener>"+item+"</convener>\n\t\t"
            res += "</conveners>\n"
        res += "\t</course>\n"
        return res