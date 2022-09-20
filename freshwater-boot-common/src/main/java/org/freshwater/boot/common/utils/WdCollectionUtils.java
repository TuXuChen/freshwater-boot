package org.freshwater.boot.common.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *  集合工具类
 *  提供list转tree, 集合比较等功能
 * @author tuxuchen
 * @date 2022/7/22 16:36
 */
public class WdCollectionUtils {

  private WdCollectionUtils() {
    throw new UnsupportedOperationException();
  }

  /**
   * 该⼯具⽅法可⽤于从两个相同泛型的集合中，依据指定的属性（由function确认）从两个集合中分离出“差集”
   * @param targetCollections 需要进⾏“差集”分析的集合
   * @param resourceCollections 依据参考的集合。
   * @param function 泛型集合中获取属性的那个⽅法。
   * @return targetCollections集合中没有存在于resouceCollections集合中的元素将被返回
   */
  public static <T , R> Set<R> collectionDifferent(Collection<T> targetCollections , Collection<T> resourceCollections , Function<T , R> function) {
    Set<R> currentTargetCollections = new HashSet<>();
    Set<R> currentResouceCollections = new HashSet<>();
    if(targetCollections != null && !targetCollections.isEmpty()) {
      currentTargetCollections = targetCollections.stream().map(function).collect(Collectors.toSet());
    }
    if(resourceCollections != null && !resourceCollections.isEmpty()) {
      currentResouceCollections = resourceCollections.stream().map(function).collect(Collectors.toSet());
    }

    // 进⾏差集⽐较
    return Sets.difference(currentTargetCollections, currentResouceCollections);
  }

  /**
   * 该⼯具⽅法可⽤于从两个相同泛型的集合中，依据指定的属性（由function确认）从两个集合中识别出“交集”
   * @param targetCollections 需要进⾏“交集”分析的集合
   * @param resourceCollections 依据参考的集合。
   * @param function 泛型集合中获取属性的那个⽅法，这个⽅法的值将作为元素在交集⽐较过程中所呈现的值。
   * @return targetCollections集合中存在，且存在于resouceCollections集合中的元素将被返回。
   */
  public static <T , R> Set<R> collectionIntersection(Collection<T> targetCollections , Collection<T> resourceCollections , Function<T , R> function) {
    Set<R> currentTargetCollections = new HashSet<>();
    Set<R> currentResourceCollections = new HashSet<>();
    if(targetCollections != null && !targetCollections.isEmpty()) {
      currentTargetCollections = targetCollections.stream().map(function).collect(Collectors.toSet());
    }
    if(resourceCollections != null && !resourceCollections.isEmpty()) {
      currentResourceCollections = resourceCollections.stream().map(function).collect(Collectors.toSet());
    }

    // 进⾏交集
    return Sets.intersection(currentTargetCollections, currentResourceCollections);
  }

  /**
   * 在业务代码的处理逻辑中，我们经常会遇上判定当前调⽤者提交的明细集合中的数据，哪些明细数据项是需要进⾏修改操作的、哪些明细数据项需要进⾏
   删除操作，哪些明细数据项需要进⾏新增操作。</p>
   * 该⼯具⽅法专⻔提供给业务开发⼈员，通过最简易的代码段就可以在任何需要的地⽅实现以上需求逻辑。</p>
   *
   * 请看以下举例：例如要找到业务表A中，若⼲明细项B，哪些需要进⾏添加操作、哪些需要进⾏修改操作、哪些需要进⾏删除操作，那么可以使⽤以下代码
   直接实现：</p>
   *
   * <code>
   * Set<B> deleteCollections = new HashSet<>();</br>
   * Set<B> updateCollections = new HashSet<>();</br>
   * Set<B> createCollections = new HashSet<>();</br>
   * nebulaToolkitService.collectionDiscrepancy(requestBCollections , dbBCollections , B::getId , deleteCollections , updateCollections , createCollections);</
   br>
   * // 调⽤成功后，deleteCollections将存储那些需要被删除的明细项</br>
   * // 调⽤成功后，updateCollections将存储那些需要被修改的明细项</br>
   * // 调⽤成功后，createCollections将存储那些需要被添加的明细项</p>
   * </code>
   *
   * @param <T> 当前作为参照函数的返回值类型
   * @param <E> 使⽤该⽅法的业务模型类型必须在技术中台进⾏了注册
   * @param currentCollections 当前要进⾏CUD操作类型拆分的原始集合，从业务开发的⻆度讲，该集合⼀般来⾃于前端⻚⾯的提交的各种明细项集合数据
   * @param referenceCollections 当前进⾏CUD操作类型拆分的参照集合，从业务开发的⻆度讲，该集合⼀般来⾃于从数据持久层查询出来的之前已经存在的
  明细项集合信息
   * @param function 参照函数，⼀般以getId为典型函数代表
   * @param deleteCollections 当前currentCollections集合中不存在但是referenceCollections集合中存在的信息，将从referenceCollections集合中复制引⽤到
  deleteCollections集合
   * @param updateCollections 当前currentCollections集合中存在，referenceCollections集合中也存在的信息，将从currentCollections集合中复制引⽤到
  updateCollections集合
   * @param createCollections 当前存在于currentCollections集合中，但是其function参照函数获取的值为null，或者为“空字符串”（只对String起作⽤）；或者
  currentCollections集合中存在但是referenceCollections集合中不存在的信息，这些数据从currentCollections集合中复制引⽤到createCollections集合
   */
  public static <T,E> void collectionDiscrepancy(Collection<E> currentCollections , Collection<E> referenceCollections , Function<E , T> function , Collection<E>
      deleteCollections , Collection<E> updateCollections, Collection<E> createCollections) {
    Validate.notNull(currentCollections, "当前进⾏CUD操作类型拆分的原始集合不能为空!!");
    Validate.notNull(function , "指定的参照函数不能为空!!");
    Validate.notNull(deleteCollections , "拆分分析完成后，⽤于承装“删除数据”的集合不能为空!!");
    Validate.notNull(updateCollections , "拆分分析完成后，⽤于承装“修改数据”的集合不能为空!!");
    Validate.notNull(createCollections , "拆分分析完成后，⽤于承装“新增数据”的集合不能为空!!");
    Validate.isTrue(referenceCollections.stream().filter(item -> {
      Object result = function.apply(item);
      if(result == null) {
        return true;
      }
      return (result instanceof String && StringUtils.isBlank((String)result));
    }).count() == 0L, "在进⾏拆分分析时，参照集合referenceCollections中各元素针对函数式[function]的返回值不能为空，请检查!!");

    // 1、⾸先判定删除集合
    // currentCollections集合中不存在但是referenceCollections集合中存在的信息
    Set<T> deleteIds = collectionDifferent(referenceCollections , currentCollections, function);
    if(deleteIds != null) {
      List<E> deleteResult = referenceCollections.stream().filter(currentItem -> deleteIds.contains(function.apply(currentItem))).collect(Collectors.toList());
      deleteCollections.addAll(deleteResult);
    }

    // 2、然后判定修改集合
    // 当前currentCollections集合中存在，referenceCollections集合中也存在的信息
    Collection<T> updateIds = collectionIntersection(currentCollections, referenceCollections, function);
    if(updateIds != null) {
      List<E> updateResult = referenceCollections.stream().filter(currentItem -> updateIds.contains(function.apply(currentItem))).collect(Collectors.toList());
      updateCollections.addAll(updateResult);
    }

    // 第⼆种情况：currentCollections集合中存在但是referenceCollections集合中不存在的信息
    Set<T> createIds = collectionDifferent(currentCollections, referenceCollections , function);
    List<E> createResult2 = null;
    if(createIds != null) {
      createResult2 = currentCollections.stream().filter(currentItem -> createIds.contains(function.apply(currentItem))).collect(Collectors.toList());
    }
    if(createResult2 != null) {
      createCollections.addAll(createResult2);
    }
  }

  /**
   * 将一维集合装换为一个树, 该操作未做任何排序操作,如果需要排序,请在转换为树结构后自定排序
   * 适合于一个对象中,用一个父级标识来标记上级的一维数据
   * 注意,如果是root节点,那么表示在列表中找不到对应的父级
   * @param list  需要转换的集合
   * @param keyMapper T对象中获取唯一标识的方法
   * @param parentIdMapper 获取父ID的方法
   * @param getChildrenMapper 获取子级的方法
   * @param setChildrenMapper 设置子级的方法
   * @return
   */
  public static <T> List<T> list2Tree(Collection<T> list, Function<? super T, String> keyMapper, Function<? super T, String> parentIdMapper,
                                      Function<? super T, List<T>> getChildrenMapper, BiFunction<T, List<T>, T> setChildrenMapper) {
    return list2Tree(list, keyMapper, parentIdMapper, getChildrenMapper, setChildrenMapper, null);
  }

  /**
   * 将一维集合装换为一个树, 该操作未做任何排序操作,如果需要排序,请在转换为树结构后自定排序
   * 适合于一个对象中,用一个父级标识来标记上级的一维数据
   * 注意,如果是root节点,那么表示在集合中找不到对应的父级
   * @param list  需要转换的集合
   * @param keyMapper T对象中获取唯一标识的方法
   * @param parentIdMapper 获取父ID的方法
   * @param getChildrenMapper 获取子级的方法
   * @param setChildrenMapper 设置子级的方法
   * @param comparator 排序规则,如果为null,则不排序
   * @return
   */
  public static <T> List<T> list2Tree(Collection<T> list, Function<? super T, String> keyMapper, Function<? super T, String> parentIdMapper,
                                      Function<? super T, List<T>> getChildrenMapper, BiFunction<T, List<T>, T> setChildrenMapper, Comparator<? super T> comparator) {
    if(CollectionUtils.isEmpty(list)) {
      return Lists.newArrayList();
    }
    List<T> root = Lists.newArrayList();
    Map<String, T> listMap = list.stream().collect(Collectors.toMap(keyMapper, v -> v));
    for (T t : list) {
      String parentId = parentIdMapper.apply(t);
      if(listMap.containsKey(parentId)) {
        T parent = listMap.get(parentId);
        List<T> children = getChildrenMapper.apply(parent);
        if(children == null) {
          children = Lists.newArrayList();
          setChildrenMapper.apply(parent, children);
        }
        children.add(t);
      } else {
        root.add(t);
      }
    }
    if(comparator != null) {
      sortTree(root, getChildrenMapper, comparator);
    }
    return root;
  }

  /**
   * 将一棵树装换为一维集合
   * @param tree
   * @param getChildrenMapper
   * @param setChildrenMapper 可以为null, 为null则不处理子级
   * @param <T>
   * @return
   */
  public static <T> List<T> tree2List(Collection<T> tree, Function<? super T, List<T>> getChildrenMapper, BiFunction<T, List<T>, T> setChildrenMapper) {
    tree = ObjectUtils.defaultIfNull(tree, Lists.newArrayList());
    List<T> list = Lists.newArrayList();
    List<T> children = Lists.newArrayList(tree);
    while (!CollectionUtils.isEmpty(children)) {
      List<T> nextChildren = Lists.newArrayList();
      for (T child : children) {
        List<T> childChildren = ObjectUtils.defaultIfNull(getChildrenMapper.apply(child), Lists.newArrayList());
        if(setChildrenMapper != null) {
          setChildrenMapper.apply(child, null);
        }
        list.add(child);
        nextChildren.addAll(childChildren);
      }
      children = nextChildren;
    }
    return list;
  }

  /**
   * 排序
   * @param list
   * @param <T>
   */
  private static  <T> void sortTree(List<T> list, Function<? super T, List<T>> getChildrenMapper, Comparator<? super T> comparator) {
    if(list == null) {
      return;
    }
    Collections.sort(list, comparator);
    for (T t : list) {
      List<T> children = getChildrenMapper.apply(t);
      sortTree(children, getChildrenMapper, comparator);
    }
  }
}
