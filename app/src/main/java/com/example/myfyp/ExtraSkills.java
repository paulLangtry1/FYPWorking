package com.example.myfyp;

public class ExtraSkills
{
    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }
    public ExtraSkills()
    {

    }

    public ExtraSkills(String userid, String skill,String skillid)
    {
       this.userid = userid;
       this.skill =skill;
       this.skillid = skillid;

    }

    String userid;
    String skill;

    public String getSkillid() {
        return skillid;
    }

    public void setSkillid(String skillid) {
        this.skillid = skillid;
    }

    String skillid;
}
