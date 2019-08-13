using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using LMS.Models.LMSModels;
using Newtonsoft.Json;

namespace LMS.Controllers
{
  public class CommonController : Controller
  {

    /*******Begin code to modify********/

    // TODO: Uncomment and change 'X' after you have scaffoled

    protected Team1LMSContext db;

    public CommonController()
    {
      db = new Team1LMSContext();
    }

    /*
     * WARNING: This is the quick and easy way to make the controller
     *          use a different LibraryContext - good enough for our purposes.
     *          The "right" way is through Dependency Injection via the constructor 
     *          (look this up if interested).
    */
    public void UseLMSContext(Team1LMSContext ctx)
    {
      db = ctx;
    }

    protected override void Dispose(bool disposing)
    {
      if (disposing)
      {
        db.Dispose();
      }
      base.Dispose(disposing);
    }
   



    /// <summary>
    /// Retreive a JSON array of all departments from the database.
    /// Each object in the array should have a field called "name" and "subject",
    /// where "name" is the department name and "subject" is the subject abbreviation.
    /// </summary>
    /// <returns>The JSON array</returns>
    public IActionResult GetDepartments()
    {
            // works
            var query = 
                    from d in db.Departments
                    select new
                    {
                        name = d.Name,
                        subject = d.Subject
                    };
            System.Diagnostics.Debug.WriteLine(query.ToList().Count);
            return Json(query.ToArray());
    }



        /// <summary>
        /// Returns a JSON array representing the course catalog.
        /// Each object in the array should have the following fields:
        /// "subject": The subject abbreviation, (e.g. "CS")
        /// "dname": The department name, as in "Computer Science"
        /// "courses": An array of JSON objects representing the courses in the department.
        ///            Each field in this inner-array should have the following fields:
        ///            "number": The course number (e.g. 5530)
        ///            "cname": The course name (e.g. "Database Systems")
        /// </summary>
        /// <returns>The JSON array</returns>
        public IActionResult GetCatalog()
        {
            // works
            var query =
                from cou in db.Courses
                join d in db.Departments on cou.Department equals d.Subject
                select new
                {
                    subject = d.Subject,
                    dname = d.Name,
                    courses =  new List<courseHelper>()
                    {
                        new courseHelper {number = cou.Num, cname = cou.Name } 
                    }
                };
            /*
            foreach (var v in query)
            {
                System.Diagnostics.Debug.WriteLine(v);
            }
            System.Diagnostics.Debug.WriteLine(query.ToList().Count);
            */
            return Json(query.ToArray());
        }

        /// <summary>
        /// Returns a JSON array of all class offerings of a specific course.
        /// Each object in the array should have the following fields:
        /// "season": the season part of the semester, such as "Fall"
        /// "year": the year part of the semester
        /// "location": the location of the class
        /// "start": the start time in format "hh:mm:ss"
        /// "end": the end time in format "hh:mm:ss"
        /// "fname": the first name of the professor
        /// "lname": the last name of the professor
        /// </summary>
        /// <param name="subject">The subject abbreviation, as in "CS"</param>
        /// <param name="number">The course number, as in 5530</param>
        /// <returns>The JSON array</returns>
        public IActionResult GetClassOfferings(string subject, int number)
        {
            // work
            var query =
                from cou in db.Courses
                where cou.Department == subject && cou.Num == number.ToString()
                join cl in db.Classes
                on cou.CatalogId equals cl.CatalogId
                into coJoinCl

                from cocl in coJoinCl
                join p in db.Professors
                on cocl.Teacher equals p.UId
                into coclJoinP

                from coclp in coclJoinP
                join u in db.Users
                on coclp.UId equals u.UId
                into coclpJoinU

                from coclpu in coclpJoinU
                select new
                {
                    season = cocl.Semester.Substring(0, cocl.Semester.Length-4),
                    year = cocl.Semester.Substring(cocl.Semester.Length-4, cocl.Semester.Length),
                    location = cocl.Loc,
                    start = cocl.Start,
                    end = cocl.End,
                    fname = coclpu.FName,
                    lname = coclpu.LName
                    };
            //System.Diagnostics.Debug.WriteLine(query.ToList().Count);
            return Json(query.ToArray());

        }

        /// <summary>
        /// This method does NOT return JSON. It returns plain text (containing html).
        /// Use "return Content(...)" to return plain text.
        /// Returns the contents of an assignment.
        /// </summary>
        /// <param name="subject">The course subject abbreviation</param>
        /// <param name="num">The course number</param>
        /// <param name="season">The season part of the semester for the class the assignment belongs to</param>
        /// <param name="year">The year part of the semester for the class the assignment belongs to</param>
        /// <param name="category">The name of the assignment category in the class</param>
        /// <param name="asgname">The name of the assignment in the category</param>
        /// <returns>The assignment contents</returns>
        public IActionResult GetAssignmentContents(string subject, int num, string season, int year, string category, string asgname)
        {
            var query =
                from cou in db.Courses
                where cou.Department == subject && cou.Num == num.ToString()
                join cl in db.Classes on cou.CatalogId equals cl.CatalogId
                where cl.Semester == season + year.ToString()
                join ac in db.AssignmentCategories on cl.ClassId equals ac.ClassId
                where ac.Name == category
                join a in db.Assignments on ac.CategoryId equals a.CategoryId
                where a.Name == asgname
                select a.Contents;

            foreach (var v in query)
            {
                System.Diagnostics.Debug.WriteLine(v);
            }
            System.Diagnostics.Debug.WriteLine(query.ToList().Count);

            return Content(query.First());
        }


        /// <summary>
        /// This method does NOT return JSON. It returns plain text (containing html).
        /// Use "return Content(...)" to return plain text.
        /// Returns the contents of an assignment submission.
        /// Returns the empty string ("") if there is no submission.
        /// </summary>
        /// <param name="subject">The course subject abbreviation</param>
        /// <param name="num">The course number</param>
        /// <param name="season">The season part of the semester for the class the assignment belongs to</param>
        /// <param name="year">The year part of the semester for the class the assignment belongs to</param>
        /// <param name="category">The name of the assignment category in the class</param>
        /// <param name="asgname">The name of the assignment in the category</param>
        /// <param name="uid">The uid of the student who submitted it</param>
        /// <returns>The submission text</returns>
        public IActionResult GetSubmissionText(string subject, int num, string season, int year, string category, string asgname, string uid)
        {
            var query =
                from cou in db.Courses
                where cou.Department == subject && cou.Num == num.ToString()
                join cl in db.Classes on cou.CatalogId equals cl.CatalogId
                where cl.Semester == season + year.ToString()
                join ac in db.AssignmentCategories on cl.ClassId equals ac.ClassId
                where ac.Name == category
                join a in db.Assignments on ac.CategoryId equals a.CategoryId
                where a.Name == asgname
                join s in db.Submission on a.AId equals s.AId
                where s.UId == uid
                select s.Contents;

            foreach (var v in query)
            {
                System.Diagnostics.Debug.WriteLine(v);
            }
            System.Diagnostics.Debug.WriteLine(query.ToList().Count);

            return query.Count()==0?Content(" ") : Content(query.First());
        }


        /// <summary>
        /// Gets information about a user as a single JSON object.
        /// The object should have the following fields:
        /// "fname": the user's first name
        /// "lname": the user's last name
        /// "uid": the user's uid
        /// "department": (professors and students only) the name (such as "Computer Science") of the department for the user. 
        ///               If the user is a Professor, this is the department they work in.
        ///               If the user is a Student, this is the department they major in.    
        ///               If the user is an Administrator, this field is not present in the returned JSON
        /// </summary>
        /// <param name="uid">The ID of the user</param>
        /// <returns>
        /// The user JSON object 
        /// or an object containing {success: false} if the user doesn't exist
        /// </returns>
        public IActionResult GetUser(string uid)
        {
            // work
            // single item. query.toArray()[0] other than query
            var qp =
                from u in db.Users
                where u.UId == uid
                join p in db.Professors on u.UId equals p.UId
                join d in db.Departments on p.Department equals d.Subject
                select new
                {
                    fname = u.FName,
                    lname = u.LName,
                    uid = u.UId,
                    department = d.Name
                };
            
            if (qp.Count() > 0)
            {
                /*
                foreach (var v in qp)
                {
                    System.Diagnostics.Debug.WriteLine(v);
                }
                */
                return Json(qp.ToArray()[0]);
            }
            
            var qs =
                from u in db.Users
                where u.UId == uid
                join s in db.Students on u.UId equals s.UId
                join d in db.Departments on s.Department equals d.Subject
                select new
                {
                    fname = u.FName,
                    lname = u.LName,
                    uid = u.UId,
                    department = d.Name
                };
            
            if (qs.Count() > 0)
            {
                /*
                foreach(var v in qs)
                {
                    System.Diagnostics.Debug.WriteLine("hello");
                    System.Diagnostics.Debug.WriteLine(v);
                }
                */
                return Json(qs.ToArray()[0]);
            }
            
            var qa =
                from u in db.Users
                where u.UId == uid
                select new
                {
                    fname = u.FName,
                    lname = u.LName,
                    uid = u.UId
                };
            
            if (qa.ToList().Count > 0)
            {
                return Json(qa.ToArray()[0]);
            }
            
            else
            return Json(new { success = false });
            
            //return qpc == 0 ? (qsc == 0 ? (qac == 0 ? (Json(new { success = false })) : Json(qa.ToArray())) : Json(qs.ToArray())) : Json(qp.ToArray());
        }


        /*******End code to modify********/

    }
}

public class courseHelper
{
    public string number { get; set; }
    public string cname { get; set; }
}
