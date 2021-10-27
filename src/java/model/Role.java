package model;

public class Role {

    private int roleID;
    private String roleName;
    private boolean canTakeExam;
    private boolean canManageAccount;
    private boolean canManageBank;
    private boolean canManageExam;

    public Role(int roleID, String roleName, boolean canTakeExam, boolean canManageAccount, boolean canManageBank, boolean canManageExam) {
        this.roleID = roleID;
        this.roleName = roleName;
        this.canTakeExam = canTakeExam;
        this.canManageAccount = canManageAccount;
        this.canManageBank = canManageBank;
        this.canManageExam = canManageExam;
    }

    public int getRoleID() {
        return roleID;
    }

    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public boolean canTakeExam() {
        return canTakeExam;
    }

    public void setCanTakeExam(boolean canTakeExam) {
        this.canTakeExam = canTakeExam;
    }

    public boolean canManageAccount() {
        return canManageAccount;
    }

    public void setCanManageAccount(boolean canManageAccount) {
        this.canManageAccount = canManageAccount;
    }

    public boolean canManageBank() {
        return canManageBank;
    }

    public void setCanManageBank(boolean canManageBank) {
        this.canManageBank = canManageBank;
    }

    public boolean canManageExam() {
        return canManageExam;
    }

    public void setCanManageExam(boolean canManageExam) {
        this.canManageExam = canManageExam;
    }

}
