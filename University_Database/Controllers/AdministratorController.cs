using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using LMS.Models.LMSModels;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace LMS.Controllers
{
    [Authorize(Roles = "Administrator")]
    public class AdministratorController : CommonController
    {
        public IActionResult Index()
        {
            return View();
        }

        public IActionResult Department(string subject)
        {
            ViewData["subject"] = subject;
            return View();
        }

        public IActionResult Course(string subject, string num)
        {
            ViewData["subject"] = subject;
            ViewData["num"] = num;
            return View();
        }

        /*******Begin code to modify********/

        /// <summary>
        /// Returns a JSON array of all the courses in the given department.
        /// Each object in the array should have the following fields:
        /// "number" - The course number (as in 5530)
        /// "name" - The course name (as in "Database Systems")
        /// </summary>
        /// <param name="subject">The department subject abbreviation (as in "CS")</param>
        /// <returns>The JSON result</returns>
        public IActionResult GetCourses(string subject)
        {
            // work
            var query =
                from c in db.Courses
                where c.Department == subject
                select new
                {
                    number = c.Num,
                    name = c.Name
                };

            /*
            foreach (var v in query)
            {
                System.Diagnostics.Debug.WriteLine(v);
            }
            */

            return Json(query.ToArray());

        }

        /// <summary>
        /// Returns a JSON array of all the professors working in a given department.
        /// Each object in the array should have the following fields:
        /// "lname" - The professor's last name
        /// "fname" - The professor's first name
        /// "uid" - The professor's uid
        /// </summary>
        /// <param name="subject">The department subject abbreviation</param>
        /// <returns>The JSON result</returns>
        public IActionResult GetProfessors(string subject)
        {
            // not shown on the screen????? can return the expected items on console
            var query =
                from p in db.Professors
                where p.Department == subject
                join user in db.Users on p.UId equals user.UId
                select new
                {
                    lname = user.LName,
                    fname = user.FName,
                    uid = user.UId
                };
            
            foreach (var v in query)
            {
                System.Diagnostics.Debug.WriteLine(v);
            }
            
            return Json(query.ToArray());

        }
        
        /// <summary>
        /// Creates a course.
        /// A course is uniquely identified by its number + the subject to which it belongs
        /// </summary>
        /// <param name="subject">The subject abbreviation for the department in which the course will be added</param>
        /// <param name="number">The course number</param>
        /// <param name="name">The course name</param>
        /// <returns>A JSON object containing {success = true/false}.
        /// false if the course already exists, true otherwise.</returns>
        public IActionResult CreateCourse(string subject, int number, string name)
        {
            // work
            var query =
                from c in db.Courses
                where c.Department == subject && c.Num == number.ToString()
                select c;
           
            if(query.ToList().Count() > 0)
            {
                return Json(new { success = false });
            }

            Courses co = new Courses();
            co.Name = name;
            co.Num = number.ToString();
            co.Department = subject;

            db.Courses.Add(co);
            db.SaveChanges();

            return Json(new { success = true });

        }
        
        /// <summary>
        /// Creates a class offering of a given course.
        /// </summary>
        /// <param name="subject">The department subject abbreviation</param>
        /// <param name="number">The course number</param>
        /// <param name="season">The season part of the semester</param>
        /// <param name="year">The year part of the semester</param>
        /// <param name="start">The start time</param>
        /// <param name="end">The end time</param>
        /// <param name="location">The location</param>
        /// <param name="instructor">The uid of the professor</param>
        /// <returns>A JSON object containing {success = true/false}. 
        /// false if another class occupies the same location during any time 
        /// within the start-end range in the same semester, or if there is already
        /// a Class offering of the same Course in the same Semester,
        /// true otherwise.</returns>
        public IActionResult CreateClass(string subject, int number, string season, int year, DateTime start, DateTime end, string location, string instructor)
        {
            //work
            var query =
                from c in db.Classes
                where c.Loc == location && ((TimeSpan.Compare(start-DateTime.Now, c.Start.Value) >= 0 && TimeSpan.Compare(start-DateTime.Now,c.End.Value) <= 0) || (TimeSpan.Compare(end-DateTime.Now, c.Start.Value) >= 0 && TimeSpan.Compare(end-DateTime.Now, c.End.Value) <= 0))
                select c;

            if(query.ToList().Count > 0)
            {
                return Json(new { success = false });
            }
            
            var query2 =
                (from c in db.Classes
                where c.Semester.Substring(0,c.Semester.IndexOf(" "))==season && c.Semester.Substring(c.Semester.IndexOf(" ")+1,c.Semester.Length)==year.ToString()
                join cou in db.Courses on c.CatalogId equals cou.CatalogId
                where cou.Num == number.ToString() && cou.Department == subject
                select c).ToList();

            if (query2.Count > 0)
            {
                return Json(new { success = false });
            }

            var query3 =
                from cou in db.Courses
                where cou.Department == subject && cou.Num == number.ToString()
                select cou.CatalogId;

            /*
            foreach (var v in query)
            {
                System.Diagnostics.Debug.WriteLine("create class: ");
                System.Diagnostics.Debug.WriteLine(v);
            }
            System.Diagnostics.Debug.WriteLine(query3.ToList().Count);
            */

            Classes cl = new Classes();
            cl.CatalogId = query3.ToList()[0]; 
            cl.Semester = season + " " + year.ToString();
            cl.Teacher = instructor;
            cl.Loc = location;
            cl.Start = start.TimeOfDay;
            cl.End = end.TimeOfDay;

            db.Classes.Add(cl);
            db.SaveChanges();

            return Json(new { success = true });
        }


        /*******End code to modify********/

    }
}