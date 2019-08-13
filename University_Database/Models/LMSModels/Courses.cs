﻿using System;
using System.Collections.Generic;

namespace LMS.Models.LMSModels
{
    public partial class Courses
    {
        public Courses()
        {
            Classes = new HashSet<Classes>();
        }

        public string Name { get; set; }
        public string Num { get; set; }
        public uint CatalogId { get; set; }
        public string Department { get; set; }

        public virtual Departments DepartmentNavigation { get; set; }
        public virtual ICollection<Classes> Classes { get; set; }
    }
}
