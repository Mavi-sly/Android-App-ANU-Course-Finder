import json
import os

import requests
from bs4 import BeautifulSoup
import re

from course import Course


def getCourses(url):
    resJSON = requests.get(url).text
    courseList = json.loads(resJSON)["Items"]
    courses = []
    for course in courseList:
        courses.append(Course(course["CourseCode"], course["Name"], course["Session"], course["ModeOfDelivery"], course["Units"]))
    return courses

# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    courses = getCourses('https://programsandcourses.anu.edu.au/data/CourseSearch/GetCourses?AppliedFilter=FilterByCourses&Source=Breadcrumb&ShowAll=true&PageIndex=0&MaxPageSize=10&PageSize=Infinity&SortColumn=&SortDirection=&InitailSearchRequestedFromExternalPage=false&SearchText=&SelectedYear=2024&Careers%5B0%5D=&Careers%5B1%5D=&Careers%5B2%5D=&Careers%5B3%5D=&Sessions%5B0%5D=&Sessions%5B1%5D=&Sessions%5B2%5D=&Sessions%5B3%5D=&Sessions%5B4%5D=&Sessions%5B5%5D=&DegreeIdentifiers%5B0%5D=&DegreeIdentifiers%5B1%5D=&DegreeIdentifiers%5B2%5D=&FilterByMajors=&FilterByMinors=&FilterBySpecialisations=&CollegeName=All+Colleges&ModeOfDelivery=All+Modes')
    for course in courses:
        url = course.getCourseUrl()
        res = requests.get(url).text
        soup = BeautifulSoup(res, "html.parser")
        summary = soup.find_all('li',class_ = 'degree-summary__code')
        for item in summary:
            list = item.contents
            if len(list) < 5:
                continue
            name = list[1].text
            value = list[3].text
            course.addAttr(name, value)

    xml = '<?xml version="1.0" ?>\n<courses>\n'
    for course in courses:
        xml += str(course)
    xml += '<\courses>'

    with open("./data.xml", 'w') as f:
        f.write(xml);