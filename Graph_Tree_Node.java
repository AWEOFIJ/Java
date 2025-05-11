
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

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

public class Graph_Tree_Node {

    private Map<Integer, List<Integer>> adjacencyList;

    public Graph_Tree_Node() {
        adjacencyList = new HashMap<>();
    }

    // 新增頂點
    public void addVertex(int vertex) {
        if (!adjacencyList.containsKey(vertex)) {
            adjacencyList.put(vertex, new ArrayList<>());
        } else {
            System.out.println("頂點已存在");
        }
    }

    // 新增邊：無向圖，因此雙向連結
    public void addEdge(int vertex1, int vertex2) {
        if (adjacencyList.containsKey(vertex1) && adjacencyList.containsKey(vertex2)) {
            if (!adjacencyList.get(vertex1).contains(vertex2)) {
                adjacencyList.get(vertex1).add(vertex2);
            }
            if (!adjacencyList.get(vertex2).contains(vertex1)) {
                adjacencyList.get(vertex2).add(vertex1);
            }
        } else {
            // 提示哪個頂點不存在
            if (!adjacencyList.containsKey(vertex1)) {
                System.out.println("第一項頂點不存在");
            } else {
                System.out.println("第二項頂點不存在");
            }
        }
    }

    // 刪除頂點：同時刪除所有與該頂點相連的邊
    public void removeVertex(int vertex) {
        if (adjacencyList.containsKey(vertex)) {
            // 為避免修改集合時產生 ConcurrentModificationException，先複製鄰居清單
            List<Integer> neighbors = new ArrayList<>(adjacencyList.get(vertex));
            for (Integer neighbor : neighbors) {
                removeEdge(vertex, neighbor);
            }
            adjacencyList.remove(vertex);
        } else {
            System.out.println("此頂點已不存在");
        }
    }

    // 刪除邊
    public void removeEdge(int vertex1, int vertex2) {
        if (adjacencyList.containsKey(vertex1)) {
            if (adjacencyList.containsKey(vertex2)) {
                // 利用 remove(Object o) 可移除該值的第一個出現
                adjacencyList.get(vertex1).remove(Integer.valueOf(vertex2));
                adjacencyList.get(vertex2).remove(Integer.valueOf(vertex1));
            } else {
                System.out.println("第二項頂點已不存在");
            }
        } else {
            System.out.println("第一項頂點已不存在");
        }
    }

    // 列印圖：印出每個頂點與其鄰居清單
    public void printGraph() {
        for (Map.Entry<Integer, List<Integer>> entry : adjacencyList.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }

    // 深度優先搜尋 (DFS)
    public List<Integer> dfs(int start) {
        List<Integer> result = new ArrayList<>();
        Stack<Integer> stack = new Stack<>();
        Set<Integer> visited = new HashSet<>();
        stack.push(start);
        visited.add(start);

        while (!stack.isEmpty()) {
            int currentVertex = stack.pop();
            result.add(currentVertex);
            // 對每個鄰居檢查是否訪問過，未訪問則加入 Stack
            for (Integer neighbor : adjacencyList.get(currentVertex)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    stack.push(neighbor);
                }
            }
        }
        return result;
    }

    // 廣度優先搜尋 (BFS)
    public List<Integer> bfs(int start) {
        List<Integer> result = new ArrayList<>();
        Queue<Integer> queue = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();
        queue.offer(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            int currentVertex = queue.poll();
            result.add(currentVertex);
            for (Integer neighbor : adjacencyList.get(currentVertex)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.offer(neighbor);
                }
            }
        }
        return result;
    }

    // 取得處於 min 到 max 之間的隨機整數
    public int getRandomInteger(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    // 測試程式
    public static void main(String[] args) {
        int N = 10;
        Graph_Tree_Node numGraph = new Graph_Tree_Node();

        // 新增 0 ~ N-1 的頂點
        for (int num = 0; num < N; num++) {
            numGraph.addVertex(num);
        }

        // 隨機新增 N 條邊
        for (int i = 0; i < N; i++) {
            int randB = numGraph.getRandomInteger(0, N - 1);
            int randC = numGraph.getRandomInteger(0, N - 1);
            numGraph.addEdge(randB, randC);
        }

        // 列印圖的鄰接表
        numGraph.printGraph();

        // 印出從頂點 0 出發之 DFS 訪問順序
        System.out.println("DFS starting from 0: " + numGraph.dfs(0));
    }
}
