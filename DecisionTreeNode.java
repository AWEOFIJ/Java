import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 決策樹的類別，其中包含判斷子和後代方法
class DecisionTreeNode {
    private String questionText; // 詢問文字
    private List<DecisionTreeNode> branches; // 分支

    public DecisionTreeNode(String questionText) {
        this.questionText = questionText;
        this.branches = new ArrayList<>();
    }

    // 添加一個分支到決策樹的類別上
    public void addBranch(DecisionTreeNode node) {
        branches.add(node);
    }

    private boolean evaluate(Map<String, String> inputData) { // 根据输入数据评估分支結果
        System.out.println("對於" + questionText + "的答案:" + inputData.getOrDefault(questionText, null));

        String answer = inputData.getOrDefault(questionText, null);
        if (answer == null) {
            System.out.println("無法识别的答案。默认回到根节点");
            return false; // 返回false以表示不使用此分支路徑
        } else if (answer.equals("")) {
            System.out.println("正确選擇" + answer);
            return true; // 返回true以表示使用此分支路徑
        } else {
            for (DecisionTreeNode branch : branches) {
                if (branch.evaluate(inputData)) {
                    System.out.println("遵行" + branch); // 执行该分支路徑
                    return true;
                } else {
                    System.out.println("不遵行" + branch); // 不执行该分支路徑
                    return false;
                }
            }
        }

        return false;
    }

    public static void main(String[] args) {
        DecisionTreeNode root = new DecisionTreeNode("你的问题"); // 將初始化一個分支结构以供評估使用
        
        ArrayList<DecisionTreeNode> yesBranches = new ArrayList<>();
        ArrayList<DecisionTreeNode> noBranches = new ArrayList<>();
        
        // 添加你的Yes分支到yesBranches列表中。

        ArrayList<String> yesBranchText = new ArrayList<>(1024); // 這是Yes分支的文字
        
        for (String branchText : yesBranchText /*your_branch_texts*/) {
            DecisionTreeNode child = new DecisionTreeNode(branchText);
            
            yesBranches.add(child); // 添加子分支到Yes分支列表中
        }
        
        for (DecisionTreeNode branch : yesBranches) {
            root.addBranch(branch);
        }

        List<DecisionTreeNode> noChildren = new ArrayList<>(); // Replace with actual no-branch children
        
        for (DecisionTreeNode noChild : noChildren) {
             root.addBranch(noChild); // 为No分支添加子分支，继续往下扩展。
        }
        
        Map<String, String> inputData = new HashMap<>();
        System.out.println("输入数据:");
        // 询问用户并添加到inputData中以供使用：
            
        root.evaluate(inputData); // 呈现适当的路径结果。
    }
}
